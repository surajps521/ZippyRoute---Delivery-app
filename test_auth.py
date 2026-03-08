import requests
import json

API_URL = "http://localhost:8080/api"

# Test registration
print("Testing registration...")
register_data = {
    "email": "test@example.com",
    "password": "Test123",
    "fullName": "Test User",
    "phoneNumber": "1234567890"
}

try:
    response = requests.post(f"{API_URL}/auth/register", json=register_data)
    print(f"Status Code: {response.status_code}")
    print(f"Response: {response.text}")
    print(f"JSON: {response.json()}")
except Exception as e:
    print(f"Error: {e}")

# Test login
print("\n\nTesting login...")
login_data = {
    "email": "test@example.com",
    "password": "Test123"
}

try:
    response = requests.post(f"{API_URL}/auth/login", json=login_data)
    print(f"Status Code: {response.status_code}")
    print(f"Response: {response.text}")
    print(f"JSON: {response.json()}")
except Exception as e:
    print(f"Error: {e}")
