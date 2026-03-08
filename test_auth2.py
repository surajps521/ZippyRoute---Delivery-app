import requests
import json

API_URL = "http://localhost:8080/api"

# Test with different emails to find a working account
test_accounts = [
    {"email": "newuser@test.com", "password": "Test123", "fullName": "New User", "phoneNumber": "9999999999"},
    {"email": "user@example.com", "password": "password123", "fullName": "Test User", "phoneNumber": "1234567890"},
    {"email": "demo@test.com", "password": "Demo123!", "fullName": "Demo User", "phoneNumber": "9876543210"},
]

print("Attempting to register new accounts and log in...\n")

for account in test_accounts:
    email = account["email"]
    password = account["password"]
    
    print(f"Testing with {email}...")
    
    # Try to register  
    try:
        response = requests.post(f"{API_URL}/auth/register", json=account)
        print(f"  Register Status: {response.status_code}")
        data = response.json()
        print(f"  Register Message: {data.get('message', 'N/A')}")
        if data.get('token'):
            print(f"  ✓ Registration successful! Token received.")
            
            # Try to login
            login_data = {"email": email, "password": password}
            response = requests.post(f"{API_URL}/auth/login", json=login_data)
            print(f"  Login Status: {response.status_code}")
            data = response.json()
            print(f"  Login Message: {data.get('message', 'N/A')}")
            if data.get('token'):
                print(f"  ✓ Login successful!")
            break
    except Exception as e:
        print(f"  Error: {e}")
    
    print()
