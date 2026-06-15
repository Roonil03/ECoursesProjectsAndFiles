from rest_framework import generics, permissions, status
from rest_framework.response import Response
from rest_framework.views import APIView
from django.contrib.auth.models import User, Group
from django.shortcuts import get_object_or_404
from .models import MenuItem, Category, Cart, Order, OrderItem
from .serializers import MenuItemSerializer, CategorySerializer, CartSerializer, OrderSerializer, UserSerializer
import datetime

class IsManager(permissions.BasePermission):
    def has_permission(self, request, view):
        if not request.user.is_authenticated:
            return False
        return request.user.groups.filter(name='Manager').exists()

class IsDeliveryCrew(permissions.BasePermission):
    def has_permission(self, request, view):
        if not request.user.is_authenticated:
            return False
        return request.user.groups.filter(name='Delivery crew').exists()

class MenuItemsView(generics.ListCreateAPIView):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer
    
    def get_permissions(self):
        if self.request.method in ['POST', 'PUT', 'PATCH', 'DELETE']:
            return [permissions.IsAuthenticated(), IsManager()]
        return [permissions.IsAuthenticated()]

class SingleMenuItemView(generics.RetrieveUpdateDestroyAPIView):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer
    
    def get_permissions(self):
        if self.request.method in ['POST', 'PUT', 'PATCH', 'DELETE']:
            return [permissions.IsAuthenticated(), IsManager()]
        return [permissions.IsAuthenticated()]

class ManagersGroupView(APIView):
    permission_classes = [permissions.IsAuthenticated, IsManager]
    
    def get(self, request):
        users = User.objects.filter(groups__name='Manager')
        serializer = UserSerializer(users, many=True)
        return Response(serializer.data)
        
    def post(self, request):
        username = request.data.get('username')
        if username:
            user = get_object_or_404(User, username=username)
            group = Group.objects.get(name='Manager')
            group.user_set.add(user)
            return Response({'message': 'User added to managers'}, status=status.HTTP_201_CREATED)
        return Response({'message': 'Username required'}, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request):
        username = request.data.get('username')
        if username:
            user = get_object_or_404(User, username=username)
            group = Group.objects.get(name='Manager')
            group.user_set.remove(user)
            return Response({'message': 'User removed from managers'}, status=status.HTTP_200_OK)
        return Response({'message': 'Username required'}, status=status.HTTP_400_BAD_REQUEST)

class DeliveryCrewGroupView(APIView):
    permission_classes = [permissions.IsAuthenticated, IsManager]
    
    def get(self, request):
        users = User.objects.filter(groups__name='Delivery crew')
        serializer = UserSerializer(users, many=True)
        return Response(serializer.data)
        
    def post(self, request):
        username = request.data.get('username')
        if username:
            user = get_object_or_404(User, username=username)
            group = Group.objects.get(name='Delivery crew')
            group.user_set.add(user)
            return Response({'message': 'User added to delivery crew'}, status=status.HTTP_201_CREATED)
        return Response({'message': 'Username required'}, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request):
        username = request.data.get('username')
        if username:
            user = get_object_or_404(User, username=username)
            group = Group.objects.get(name='Delivery crew')
            group.user_set.remove(user)
            return Response({'message': 'User removed from delivery crew'}, status=status.HTTP_200_OK)
        return Response({'message': 'Username required'}, status=status.HTTP_400_BAD_REQUEST)

class CartView(generics.ListCreateAPIView):
    serializer_class = CartSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return Cart.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
        
    def delete(self, request):
        Cart.objects.filter(user=request.user).delete()
        return Response({'message': 'Cart cleared'}, status=status.HTTP_204_NO_CONTENT)

class OrdersView(generics.ListCreateAPIView):
    serializer_class = OrderSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.groups.filter(name='Manager').exists():
            return Order.objects.all()
        elif self.request.user.groups.filter(name='Delivery crew').exists():
            return Order.objects.filter(delivery_crew=self.request.user)
        else:
            return Order.objects.filter(user=self.request.user)

    def perform_create(self, serializer):
        cart_items = Cart.objects.filter(user=self.request.user)
        if not cart_items.exists():
            raise serializers.ValidationError("Cart is empty")
        
        total = sum([item.price for item in cart_items])
        order = serializer.save(user=self.request.user, total=total, date=datetime.date.today())
        
        for cart_item in cart_items:
            OrderItem.objects.create(
                order=order,
                menuitem=cart_item.menuitem,
                quantity=cart_item.quantity,
                unit_price=cart_item.unit_price,
                price=cart_item.price
            )
        cart_items.delete()

class SingleOrderView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = OrderSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.groups.filter(name='Manager').exists():
            return Order.objects.all()
        elif self.request.user.groups.filter(name='Delivery crew').exists():
            return Order.objects.filter(delivery_crew=self.request.user)
        else:
            return Order.objects.filter(user=self.request.user)

    def update(self, request, *args, **kwargs):
        if request.user.groups.filter(name='Manager').exists():
            return super().update(request, *args, **kwargs)
        elif request.user.groups.filter(name='Delivery crew').exists():
            order = self.get_object()
            order.status = request.data.get('status', order.status)
            order.save()
            return Response({'message': 'Status updated'})
        else:
            return Response(status=status.HTTP_403_FORBIDDEN)

    def delete(self, request, *args, **kwargs):
        if request.user.groups.filter(name='Manager').exists():
            return super().destroy(request, *args, **kwargs)
        return Response(status=status.HTTP_403_FORBIDDEN)
