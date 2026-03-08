#!/bin/bash

# ZippyRoute - Sample Data Initialization Script
# This script adds sample menu items to the database

API_BASE_URL="http://localhost:8080/api"

echo "🚀 Starting ZippyRoute Sample Data Initialization..."
echo "=================================================="

# Sample menu items
declare -a ITEMS=(
    '{"name":"Margherita Pizza","description":"Fresh tomato basil and mozzarella","price":299,"category":"Pizza"}'
    '{"name":"Pepperoni Pizza","description":"Loaded with pepperoni and cheese","price":349,"category":"Pizza"}'
    '{"name":"Veggie Burger","description":"Fresh vegetables with wholegrain bun","price":199,"category":"Burgers"}'
    '{"name":"Chicken Burger","description":"Grilled chicken breast with special sauce","price":229,"category":"Burgers"}'
    '{"name":"Biryani","description":"Fragrant basmati rice with spices","price":249,"category":"Indian"}'
    '{"name":"Butter Chicken","description":"Chicken in creamy tomato sauce","price":319,"category":"Indian"}'
    '{"name":"Ceasar Salad","description":"Crisp romaine with parmesan and croutons","price":179,"category":"Salads"}'
    '{"name":"Greek Salad","description":"Feta cheese olives and tomatoes","price":189,"category":"Salads"}'
    '{"name":"Chocolate Brownie","description":"Fudgy chocolate with nuts","price":99,"category":"Desserts"}'
    '{"name":"Ice Cream Sundae","description":"Vanilla ice cream with toppings","price":129,"category":"Desserts"}'
    '{"name":"Coca Cola","description":"Chilled cola beverage 250ml","price":49,"category":"Beverages"}'
    '{"name":"Fresh Lemonade","description":"Freshly squeezed with ice","price":79,"category":"Beverages"}'
)

# Function to create menu item
create_menu_item() {
    local item=$1
    echo "Adding: $item"
    curl -s -X POST "$API_BASE_URL/menu/create" \
        -H "Content-Type: application/json" \
        -d "$item" > /dev/null
    
    if [ $? -eq 0 ]; then
        echo "✓ Added successfully"
    else
        echo "✗ Failed to add item"
    fi
}

# Wait for server to be ready
echo "⏳ Waiting for API server to be ready..."
sleep 2

# Create all menu items
for item in "${ITEMS[@]}"
do
    create_menu_item "$item"
    sleep 0.5
done

echo ""
echo "=================================================="
echo "✅ Sample data initialization completed!"
echo "You can now login and browse the menu"
echo "=================================================="
