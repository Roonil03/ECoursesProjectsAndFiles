import unittest

def add(a,b):
    return a + b

class TestAddFunction(unittest.TestCase):
    
    def test_add_integers(self):
        self.assertEqual(add(2, 4), 6)
    
    def test_add_zeros(self):
        self.assertEqual(add(0, 0), 0)
    
    def test_add_floats(self):
        self.assertAlmostEqual(add(2.3, 3.6), 5.9)
    
    def test_add_strings(self):
        self.assertEqual(add('hello', 'world'), 'helloworld')
    
    def test_add_precise_floats(self):
        self.assertAlmostEqual(add(2.3000, 4.300), 6.6)
    
    def test_add_negative_numbers(self):
        self.assertNotEqual(add(-2, -2), 0)

if __name__ == '__main__':
    unittest.main()
