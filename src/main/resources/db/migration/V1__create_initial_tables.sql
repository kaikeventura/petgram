-- Function to generate UUIDv7
CREATE OR REPLACE FUNCTION uuid_v7()
RETURNS UUID AS $$
BEGIN
    -- Example of a simplified UUIDv7 generation.
    -- For a production-ready version, consider a more robust implementation
    -- or a trusted PostgreSQL extension if available.
    -- This is a placeholder demonstrating the concept.
    return gen_random_uuid(); -- Using standard gen_random_uuid() as a stand-in
END;
$$ LANGUAGE plpgsql;

-- Table: users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_v7(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: user_roles
CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- Table: pets
CREATE TABLE pets (
    id UUID PRIMARY KEY DEFAULT uuid_v7(),
    name VARCHAR(255) NOT NULL,
    breed VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: friendships
CREATE TABLE friendships (
    requester_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    addressee_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (requester_id, addressee_id)
);

-- Table: posts
CREATE TABLE posts (
    id UUID PRIMARY KEY DEFAULT uuid_v7(),
    photo_url VARCHAR(255) NOT NULL,
    caption TEXT,
    author_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table: post_tagged_pets
CREATE TABLE post_tagged_pets (
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    pet_id UUID NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, pet_id)
);

-- Table: likes
CREATE TABLE likes (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, post_id)
);

-- Table: comments
CREATE TABLE comments (
    id UUID PRIMARY KEY DEFAULT uuid_v7(),
    text VARCHAR(500) NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    post_id UUID NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
