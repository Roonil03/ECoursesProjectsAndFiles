from django.db import models

# Create your models here.
class Booking(models.Model):
    first_name = models.CharField(max_length=200)
    last_name = models.CharField(max_length=200)
    guest_count = models.IntegerField()
    # auto_now=True automatically sets this field to the current date on save
    reservation_time = models.DateField(auto_now=True)
    comments = models.CharField(max_length=1000)

    def __str__(self):
        return f"{self.first_name} {self.last_name} - Guests: {self.guest_count}"