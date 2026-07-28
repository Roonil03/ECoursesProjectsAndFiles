import * as React from 'react';
import { View, Text, Image, TextInput, Pressable, FlatList, StyleSheet, ActivityIndicator } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const INITIAL_MENU_DATA = [
  {
    id: 1,
    name: 'Greek Salad',
    price: '$12.99',
    description: 'Our delicious salad is served with Feta cheese and peeled cucumber. Includes tomatoes, onions, olives, and extra virgin olive oil.',
    image: require('../assets/splash.png'),
    category: 'starters',
  },
  {
    id: 2,
    name: 'Bruschetta',
    price: '$7.99',
    description: 'Delicious grilled bread topped with garlic, tomatoes, olive oil and fresh basil.',
    image: require('../assets/splash.png'),
    category: 'starters',
  },
  {
    id: 3,
    name: 'Grilled Fish',
    price: '$20.00',
    description: 'Our tender grilled fish seasoned with fresh herbs and lemon zest, served with a side of roasted vegetables.',
    image: require('../assets/splash.png'),
    category: 'mains',
  },
  {
    id: 4,
    name: 'Pasta Carbonara',
    price: '$14.99',
    description: 'Classic Italian pasta with creamy egg sauce, pecorino cheese, crisp pancetta, and black pepper.',
    image: require('../assets/splash.png'),
    category: 'mains',
  },
  {
    id: 5,
    name: 'Lemon Dessert',
    price: '$6.99',
    description: 'Traditional homemade lemon tart with a buttery shortbread crust and delicate meringue topping.',
    image: require('../assets/splash.png'),
    category: 'desserts',
  },
  {
    id: 6,
    name: 'Baklava',
    price: '$5.99',
    description: 'Rich, sweet pastry made of layers of filo filled with chopped nuts and sweetened with honey syrup.',
    image: require('../assets/splash.png'),
    category: 'desserts',
  },
  {
    id: 7,
    name: 'House Wine',
    price: '$8.00',
    description: 'A glass of our signature house red or white wine sourced from Mediterranean vineyards.',
    image: require('../assets/splash.png'),
    category: 'drinks',
  },
  {
    id: 8,
    name: 'Fresh Lemonade',
    price: '$4.50',
    description: 'Refreshing squeezed lemonade made with mint leaves and a touch of organic honey.',
    image: require('../assets/splash.png'),
    category: 'drinks',
  },
];

const CATEGORIES = ['Starters', 'Mains', 'Desserts', 'Drinks'];

const HomeScreen = ({ navigation }) => {
  const [menuData, setMenuData] = React.useState(INITIAL_MENU_DATA);
  const [searchQuery, setSearchQuery] = React.useState('');
  const [selectedCategories, setSelectedCategories] = React.useState([]);
  const [userProfile, setUserProfile] = React.useState(null);

  React.useEffect(() => {
    const loadProfile = async () => {
      try {
        const firstName = await AsyncStorage.getItem('firstName');
        const lastName = await AsyncStorage.getItem('lastName');
        const avatar = await AsyncStorage.getItem('avatar');
        setUserProfile({
          firstName: firstName || 'User',
          lastName: lastName || '',
          avatar: avatar || null,
        });
      } catch (e) {
        console.error('Error loading profile in Home:', e);
      }
    };
    loadProfile();
    const unsubscribe = navigation.addListener('focus', loadProfile);
    return unsubscribe;
  }, [navigation]);

  const handleCategoryPress = (categoryName) => {
    const lower = categoryName.toLowerCase();
    if (selectedCategories.includes(lower)) {
      setSelectedCategories(selectedCategories.filter((c) => c !== lower));
    } else {
      setSelectedCategories([...selectedCategories, lower]);
    }
  };

  const filteredMenu = React.useMemo(() => {
    return menuData.filter((item) => {
      const matchesSearch =
        searchQuery.trim() === '' ||
        item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        item.description.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesCategory =
        selectedCategories.length === 0 ||
        selectedCategories.includes(item.category.toLowerCase());
      return matchesSearch && matchesCategory;
    });
  }, [menuData, searchQuery, selectedCategories]);

  const renderMenuItem = ({ item }) => (
    <View style={styles.menuItemContainer}>
      <View style={styles.menuItemDetails}>
        <Text style={styles.menuItemTitle}>{item.name}</Text>
        <Text style={styles.menuItemDescription} numberOfLines={2}>
          {item.description}
        </Text>
        <Text style={styles.menuItemPrice}>{item.price}</Text>
      </View>
      <Image source={item.image} style={styles.menuItemImage} resizeMode="cover" />
    </View>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <View style={styles.headerSpacer} />
        <Image
          source={require('../assets/little-lemon-logo.png')}
          style={styles.headerLogo}
          resizeMode="contain"
        />
        <Pressable style={styles.avatarButton} onPress={() => navigation.navigate('Profile')}>
          <Image
            source={require('../assets/profile.png')}
            style={styles.avatarImage}
          />
        </Pressable>
      </View>

      <View style={styles.heroSection}>
        <Text style={styles.heroTitle}>Little Lemon</Text>
        <View style={styles.heroContent}>
          <View style={styles.heroTextContainer}>
            <Text style={styles.heroSubtitle}>Chicago</Text>
            <Text style={styles.heroDescription}>
              We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.
            </Text>
          </View>
          <Image
            source={require('../assets/splash.png')}
            style={styles.heroImage}
            resizeMode="cover"
          />
        </View>
        <View style={styles.searchContainer}>
          <TextInput
            style={styles.searchInput}
            placeholder="Search our delicious menu..."
            placeholderTextColor="#333333"
            value={searchQuery}
            onChangeText={setSearchQuery}
          />
        </View>
      </View>

      <View style={styles.breakdownSection}>
        <Text style={styles.breakdownTitle}>ORDER FOR DELIVERY!</Text>
        <View style={styles.categoriesContainer}>
          {CATEGORIES.map((category) => {
            const isSelected = selectedCategories.includes(category.toLowerCase());
            return (
              <Pressable
                key={category}
                style={isSelected ? styles.categoryButtonActive : styles.categoryButton}
                onPress={() => handleCategoryPress(category)}
              >
                <Text style={isSelected ? styles.categoryTextActive : styles.categoryText}>
                  {category}
                </Text>
              </Pressable>
            );
          })}
        </View>
      </View>

      <FlatList
        data={filteredMenu}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderMenuItem}
        contentContainerStyle={styles.listContent}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#EE9972',
  },
  headerSpacer: {
    width: 44,
  },
  headerLogo: {
    width: 160,
    height: 40,
  },
  avatarButton: {
    width: 44,
    height: 44,
    borderRadius: 22,
    overflow: 'hidden',
    borderWidth: 1.5,
    borderColor: '#495E57',
    justifyContent: 'center',
    alignItems: 'center',
  },
  avatarImage: {
    width: 44,
    height: 44,
  },
  heroSection: {
    backgroundColor: '#495E57',
    paddingHorizontal: 16,
    paddingTop: 16,
    paddingBottom: 20,
  },
  heroTitle: {
    fontSize: 40,
    fontWeight: 'bold',
    color: '#F4CE14',
  },
  heroContent: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  heroTextContainer: {
    flex: 1,
    paddingRight: 16,
  },
  heroSubtitle: {
    fontSize: 24,
    fontWeight: '600',
    color: '#FFFFFF',
    marginBottom: 8,
  },
  heroDescription: {
    fontSize: 16,
    color: '#EDEFEE',
    lineHeight: 22,
  },
  heroImage: {
    width: 120,
    height: 120,
    borderRadius: 12,
  },
  searchContainer: {
    marginTop: 8,
  },
  searchInput: {
    backgroundColor: '#EDEFEE',
    height: 44,
    borderRadius: 8,
    paddingHorizontal: 16,
    fontSize: 16,
    color: '#333333',
  },
  breakdownSection: {
    paddingHorizontal: 16,
    paddingVertical: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#EDEFEE',
  },
  breakdownTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333333',
    marginBottom: 12,
  },
  categoriesContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  categoryButton: {
    backgroundColor: '#EDEFEE',
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: 16,
  },
  categoryButtonActive: {
    backgroundColor: '#495E57',
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: 16,
  },
  categoryText: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#495E57',
  },
  categoryTextActive: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#EDEFEE',
  },
  listContent: {
    paddingHorizontal: 16,
    paddingBottom: 24,
  },
  menuItemContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 16,
  },
  menuItemDetails: {
    flex: 1,
    paddingRight: 16,
  },
  menuItemTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#333333',
    marginBottom: 4,
  },
  menuItemDescription: {
    fontSize: 14,
    color: '#495E57',
    marginBottom: 8,
    lineHeight: 20,
  },
  menuItemPrice: {
    fontSize: 16,
    fontWeight: '600',
    color: '#495E57',
  },
  menuItemImage: {
    width: 80,
    height: 80,
    borderRadius: 8,
  },
  separator: {
    height: 1,
    backgroundColor: '#EDEFEE',
  },
});

export default HomeScreen;
