import requests
import os
import random
from datetime import date, timedelta

# --- Configuration ---
BASE_URL = "http://localhost:8080"
PHOTOS_DIR = os.path.join(os.path.dirname(__file__), '..', 'fotos')

# --- Data to Seed ---
USERS_TO_CREATE = [
    {"name": "Alice", "email": "alice@example.com", "password": "password123"},
    {"name": "Bob", "email": "bob@example.com", "password": "password123"},
    {"name": "Charlie", "email": "charlie@example.com", "password": "password123"},
    {"name": "Dunha", "email": "dunha@gmail.com", "password": "123456"}
]

PETS_TO_CREATE = {
    "alice@example.com": [
        {"name": "Rex", "breed": "Golden Retriever", "birthDate": "2020-01-15"},
        {"name": "Whiskers", "breed": "Siamese", "birthDate": "2021-05-20"}
    ],
    "bob@example.com": [
        {"name": "Buddy", "breed": "Labrador", "birthDate": "2019-03-10"}
    ],
    "charlie@example.com": [
        {"name": "Lucy", "breed": "Beagle", "birthDate": "2022-08-01"},
        {"name": "Milo", "breed": "Poodle", "birthDate": "2018-11-11"}
    ],
    "dunha@gmail.com": [
        {"name": "Zoe", "breed": "Shiba Inu", "birthDate": "2021-09-10"},
        {"name": "Leo", "breed": "Maine Coon", "birthDate": "2020-02-25"}
    ]
}

POST_CAPTIONS = [
    "Just enjoying the sun! ☀️",
    "Nap time is the best time.",
    "Ready for a walk!",
    "What's for dinner?",
    "New toy! 🧸",
    "Feeling cute today.",
    "Park adventures!",
    "Missing my friends."
]

# --- Helper Functions ---

def get_photo_paths():
    print("""Gets a list of absolute paths for photos in the photos directory.""")
    if not os.path.exists(PHOTOS_DIR):
        print(f"Error: Directory not found at '{PHOTOS_DIR}'")
        return []
    return [os.path.join(PHOTOS_DIR, f) for f in os.listdir(PHOTOS_DIR) if f.lower().endswith(('png', 'jpg', 'jpeg'))]

def register_user(user_data):
    print("""Registers a user and returns the response.""")
    print(f"Registering user: {user_data['email']}")
    try:
        response = requests.post(f"{BASE_URL}/auth/register", json=user_data)
        response.raise_for_status()
        return response
    except requests.exceptions.RequestException as e:
        print(f"  Could not register user {user_data['email']}. Maybe they already exist? Error: {e}")
        return None

def login_user(email, password):
    print("""Logs in a user and returns the auth token.""")
    print(f"Logging in user: {email}")
    try:
        response = requests.post(f"{BASE_URL}/auth/login", json={"email": email, "password": password})
        response.raise_for_status()
        return response.json().get("accessToken")
    except requests.exceptions.RequestException as e:
        print(f"  Could not log in user {email}. Error: {e}")
        return None

def create_pet(token, pet_data):
    print("""Creates a pet and returns the created pet's data.""")
    print(f"  Creating pet: {pet_data['name']}")
    headers = {"Authorization": f"Bearer {token}"}
    try:
        response = requests.post(f"{BASE_URL}/pets", headers=headers, json=pet_data)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"    Could not create pet {pet_data['name']}. Error: {e}")
        return None

def upload_avatar(token, pet_id, photo_path):
    """Uploads an avatar for a pet."""
    print(f"    Uploading avatar for pet ID: {pet_id}")
    headers = {"Authorization": f"Bearer {token}"}
    with open(photo_path, 'rb') as f:
        files = {'file': (os.path.basename(photo_path), f)}
        try:
            response = requests.put(f"{BASE_URL}/pets/{pet_id}/avatar", headers=headers, files=files)
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            print(f"      Could not upload avatar. Error: {e}")

def create_post(token, pet_id, caption, photo_path):
    """Creates a post for a pet."""
    print(f"    Creating post for pet ID: {pet_id}")
    headers = {
        "Authorization": f"Bearer {token}",
        "X-Pet-Id": pet_id
    }
    with open(photo_path, 'rb') as f:
        files = {'file': (os.path.basename(photo_path), f)}
        data = {'caption': caption}
        try:
            response = requests.post(f"{BASE_URL}/posts", headers=headers, files=files, data=data)
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            print(f"      Could not create post. Error: {e}")

def send_friend_request(token, requester_pet_id, addressee_pet_id):
    """Sends a friend request from one pet to another."""
    print(f"  Pet {requester_pet_id} sending friend request to {addressee_pet_id}")
    headers = {
        "Authorization": f"Bearer {token}",
        "X-Pet-Id": requester_pet_id
    }
    json_data = {"addresseePetId": addressee_pet_id}
    try:
        response = requests.post(f"{BASE_URL}/friendships/request", headers=headers, json=json_data)
        response.raise_for_status()
        return True
    except requests.exceptions.RequestException as e:
        print(f"    Could not send friend request. Error: {e}")
        return False

def accept_friend_request(token, addressee_pet_id, requester_pet_id):
    """Accepts a friend request."""
    print(f"  Pet {addressee_pet_id} accepting friend request from {requester_pet_id}")
    headers = {
        "Authorization": f"Bearer {token}",
        "X-Pet-Id": addressee_pet_id
    }
    json_data = {"requesterPetId": requester_pet_id}
    try:
        response = requests.post(f"{BASE_URL}/friendships/accept", headers=headers, json=json_data)
        response.raise_for_status()
        return True
    except requests.exceptions.RequestException as e:
        print(f"    Could not accept friend request. Error: {e}")
        return False

# --- Main Execution ---

def main():
    print("--- Starting Database Seeding ---")
    photo_paths = get_photo_paths()
    if not photo_paths:
        print("No photos found in 'fotos' directory. Aborting.")
        return

    created_data = {"users": {}, "pets": []}

    # 1. Create Users and Pets
    for user_data in USERS_TO_CREATE:
        register_user(user_data)
        token = login_user(user_data["email"], user_data["password"])
        print(token)
        if not token:
            continue

        created_data["users"][user_data["email"]] = {"token": token, "pets": []}

        pets_for_user = PETS_TO_CREATE.get(user_data["email"], [])
        for pet_data in pets_for_user:
            created_pet = create_pet(token, pet_data)
            print(created_pet)
            if created_pet:
                pet_id = created_pet["id"]
                created_data["users"][user_data["email"]]["pets"].append(created_pet)
                created_data["pets"].append({
                    "id": pet_id,
                    "name": created_pet["name"],
                    "owner_email": user_data["email"]
                })
                # Upload avatar
                upload_avatar(token, pet_id, random.choice(photo_paths))

    print("\n--- Users and Pets Created ---")
    all_pets = created_data["pets"]
    if len(all_pets) < 2:
        print("Not enough pets to create friendships or posts. Exiting.")
        return

    # 2. Create Posts
    print("\n--- Creating Posts ---")
    for pet in all_pets:
        owner_email = pet["owner_email"]
        token = created_data["users"][owner_email]["token"]
        # Create 1-3 posts per pet
        for _ in range(random.randint(1, 3)):
            create_post(
                token,
                pet["id"],
                random.choice(POST_CAPTIONS),
                random.choice(photo_paths)
            )

    # 3. Create Friendships
    print("\n--- Creating Friendships ---")
    # Make Rex friends with Buddy and Lucy
    pet_rex = next((p for p in all_pets if p["name"] == "Rex"), None)
    pet_buddy = next((p for p in all_pets if p["name"] == "Buddy"), None)
    pet_lucy = next((p for p in all_pets if p["name"] == "Lucy"), None)
    pet_zoe = next((p for p in all_pets if p["name"] == "Zoe"), None)


    if pet_rex and pet_buddy:
        # Rex requests Buddy
        token_rex_owner = created_data["users"][pet_rex["owner_email"]]["token"]
        token_buddy_owner = created_data["users"][pet_buddy["owner_email"]]["token"]
        if send_friend_request(token_rex_owner, pet_rex["id"], pet_buddy["id"]):
            # Buddy accepts Rex
            accept_friend_request(token_buddy_owner, pet_buddy["id"], pet_rex["id"])

    if pet_rex and pet_lucy:
        # Lucy requests Rex
        token_lucy_owner = created_data["users"][pet_lucy["owner_email"]]["token"]
        token_rex_owner = created_data["users"][pet_rex["owner_email"]]["token"]
        if send_friend_request(token_lucy_owner, pet_lucy["id"], pet_rex["id"]):
            # Rex accepts Lucy
            accept_friend_request(token_rex_owner, pet_rex["id"], pet_lucy["id"])

    if pet_zoe and pet_rex:
        # Zoe requests Rex
        token_zoe_owner = created_data["users"][pet_zoe["owner_email"]]["token"]
        token_rex_owner = created_data["users"][pet_rex["owner_email"]]["token"]
        if send_friend_request(token_zoe_owner, pet_zoe["id"], pet_rex["id"]):
            # Rex accepts Zoe
            accept_friend_request(token_rex_owner, pet_rex["id"], pet_zoe["id"])


    print("\n--- Database Seeding Complete ---")


if __name__ == "__main__":
    main()
