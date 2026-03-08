// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;
let cart = JSON.parse(localStorage.getItem('cart')) || [];
let menuItems = [];

// Toast Notification Function
function showNotification(message, duration = 3000) {
    // Remove existing notifications
    const existing = document.querySelector('.toast-notification');
    if (existing) existing.remove();
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = 'toast-notification';
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 12px 20px;
        border-radius: 4px;
        font-weight: 500;
        z-index: 9998;
        animation: slideIn 0.3s ease;
        box-shadow: 0 2px 8px rgba(0,0,0,0.2);
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after duration
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => notification.remove(), 300);
    }, duration);
}

// Loading Screen Handler
function hideLoadingScreen() {
    const loadingScreen = document.getElementById('loadingScreen');
    if (loadingScreen) {
        loadingScreen.classList.add('hidden');
    }
}

function updateLoadingStatus(message) {
    const statusEl = document.getElementById('loadStatus');
    if (statusEl) {
        statusEl.textContent = message;
    }
}

// Page Navigation
function goToPage(pageName) {
    // Hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // Show navbar menu appropriately
    const navbarMenu = document.getElementById('navbarMenu');
    if (pageName === 'auth' || pageName === 'home') {
        navbarMenu.style.display = 'none';
    } else {
        navbarMenu.style.display = 'flex';
    }

    // Show selected page
    const pageId = pageName === 'auth' ? 'authPage' : pageName + 'Page';
    const page = document.getElementById(pageId);
    if (page) {
        page.classList.add('active');

        // Load data for specific pages
        if (pageName === 'menu') {
            loadMenu();
            updateCartDisplay();
        } else if (pageName === 'orders') {
            loadUserOrders();
        }
    }
}

// Navigate to Login or Menu based on auth status
function goToLoginOrMenu() {
    if (authToken && currentUser) {
        goToPage('menu');
    } else {
        goToPage('auth');
    }
}

// Authentication
function toggleAuth() {
    const authForm = document.getElementById('authForm');
    const authTitle = document.getElementById('authTitle');
    const registerFields = document.getElementById('registerFields');
    const authToggle = document.querySelector('.auth-toggle');

    if (authTitle.textContent === 'Login') {
        authTitle.textContent = 'Register';
        registerFields.style.display = 'block';
        authForm.querySelector('button').textContent = 'Register';
        authToggle.innerHTML = 'Already have an account? <a href="#" onclick="toggleAuth()">Login</a>';
    } else {
        authTitle.textContent = 'Login';
        registerFields.style.display = 'none';
        authForm.querySelector('button').textContent = 'Login';
        authToggle.innerHTML = "Don't have an account? <a href=\"#\" onclick=\"toggleAuth()\">Register</a>";
    }
}

// Initialize auth form submit handler when DOM is ready
function initAuthForm() {
    const authForm = document.getElementById('authForm');
    if (authForm) {
        authForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();
            const isLogin = document.getElementById('authTitle').textContent === 'Login';
            const submitBtn = document.querySelector('#authForm button[type="submit"]');
            const originalText = submitBtn.textContent;

    // Remove any existing error/success messages
    const existingError = document.querySelector('.auth-error-message');
    const existingSuccess = document.querySelector('.auth-success-message');
    if (existingError) existingError.remove();
    if (existingSuccess) existingSuccess.remove();

    // Validate email and password
    if (!email) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'auth-error-message';
        errorDiv.textContent = 'Please enter your email address';
        errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
        document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
        return;
    }

    if (!password) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'auth-error-message';
        errorDiv.textContent = 'Please enter your password';
        errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
        document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
        return;
    }

    if (!isLogin) {
        const fullName = document.getElementById('fullName').value.trim();
        const phoneNumber = document.getElementById('phoneNumber').value.trim();

        if (!fullName) {
            const errorDiv = document.createElement('div');
            errorDiv.className = 'auth-error-message';
            errorDiv.textContent = 'Please enter your full name';
            errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
            document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
            return;
        }

        if (!phoneNumber) {
            const errorDiv = document.createElement('div');
            errorDiv.className = 'auth-error-message';
            errorDiv.textContent = 'Please enter your phone number';
            errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
            document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
            return;
        }
    }

    // Show loading state
    submitBtn.textContent = 'Loading...';
    submitBtn.disabled = true;

    try {
        let url, data;

        if (isLogin) {
            url = '/auth/login';
            data = { email, password };
        } else {
            url = '/auth/register';
            data = {
                email,
                password,
                fullName: document.getElementById('fullName').value,
                phoneNumber: document.getElementById('phoneNumber').value
            };
        }

        const response = await fetch(API_BASE_URL + url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok && result.token) {
            authToken = result.token;
            currentUser = {
                id: result.userId,
                email: result.email,
                fullName: result.fullName
            };

            localStorage.setItem('authToken', authToken);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));

            // Show success message
            const successMsg = isLogin ? 'Login successful! Redirecting...' : 'Registration successful! Logging you in...';
            const successDiv = document.createElement('div');
            successDiv.className = 'auth-success-message';
            successDiv.textContent = successMsg;
            successDiv.style.cssText = 'color: #228B22; background: #E8F5E9; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
            document.querySelector('.auth-card').insertBefore(successDiv, document.querySelector('.auth-card').firstChild);

            document.getElementById('authForm').reset();
            setTimeout(() => {
                goToPage('menu');
            }, 1500);
        } else {
            // Show error message to user
            const errorMsg = result.message || 'Authentication failed. Please try again.';
            const errorDiv = document.createElement('div');
            errorDiv.className = 'auth-error-message';
            errorDiv.textContent = errorMsg;
            errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
            document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
            console.error('Authentication failed:', result.message);
        }
    } catch (error) {
        // Show network error
        const errorDiv = document.createElement('div');
        errorDiv.className = 'auth-error-message';
        errorDiv.textContent = 'Connection error. Make sure the server is running.';
        errorDiv.style.cssText = 'color: #d32f2f; background: #ffebee; padding: 10px; border-radius: 4px; margin-bottom: 15px; text-align: center; font-weight: 500;';
        document.querySelector('.auth-card').insertBefore(errorDiv, document.querySelector('.auth-card').firstChild);
        console.error('Auth error:', error);
    } finally {
        // Reset button state
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    }
        });
    }
}

function logout() {
    authToken = null;
    currentUser = null;
    cart = [];
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('cart');
    goToPage('home');
}

// Menu Management
async function loadMenu() {
    try {
        const response = await fetch(API_BASE_URL + '/menu/available', {
            headers: { 'Authorization': 'Bearer ' + authToken }
        });

        if (!response.ok) {
            throw new Error('Failed to load menu');
        }

        menuItems = await response.json();
        displayMenu(menuItems);
        loadCategories();
    } catch (error) {
        console.error('Menu loading error:', error);
        document.getElementById('menuGrid').innerHTML = '<div class="error-message">Failed to load menu items</div>';
    }
}

async function loadCategories() {
    try {
        const response = await fetch(API_BASE_URL + '/menu/categories', {
            headers: { 'Authorization': 'Bearer ' + authToken }
        });

        if (response.ok) {
            const categories = await response.json();
            const categoryFilter = document.getElementById('categoryFilter');
            categoryFilter.innerHTML = '<option value="">All Categories</option>';

            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category;
                option.textContent = category;
                categoryFilter.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Categories loading error:', error);
    }
}

function displayMenu(items) {
    const menuGrid = document.getElementById('menuGrid');
    menuGrid.innerHTML = '';

    if (items.length === 0) {
        menuGrid.innerHTML = '<div class="empty-state"><h3>No items found</h3></div>';
        return;
    }

    items.forEach(item => {
        const cartItem = cart.find(c => c.id === item.id) || {};
        const menuItemDiv = document.createElement('div');
        menuItemDiv.className = 'menu-item';
        menuItemDiv.innerHTML = `
            <div class="menu-item-image"><img src="${item.imageUrl || 'data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22300%22 height=%22300%22%3E%3Crect fill=%22%23e0e0e0%22 width=%22300%22 height=%22300%22/%3E%3C/svg%3E'}" alt="${item.name}" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22300%22 height=%22300%22%3E%3Crect fill=%22%23e0e0e0%22 width=%22300%22 height=%22300%22/%3E%3C/svg%3E'"></div>
            <div class="menu-item-content">
                <div class="menu-item-name">${item.name}</div>
                <div class="menu-item-description">${item.description || 'Delicious food item'}</div>
                <div class="menu-item-price">₹${item.price}</div>
                <div class="menu-item-rating">⭐ ${item.rating} (${item.reviewCount} reviews)</div>
                <div class="menu-item-controls">
                    <div class="quantity-control">
                        <button onclick="decrementQuantity(${item.id})">−</button>
                        <input type="number" value="${cartItem.quantity || 0}" id="qty-${item.id}" readonly>
                        <button onclick="incrementQuantity(${item.id})">+</button>
                    </div>
                    <button class="add-to-cart-btn" onclick="addToCart(${item.id}, '${item.name}', ${item.price})">Add</button>
                </div>
            </div>
        `;
        menuGrid.appendChild(menuItemDiv);
    });
}

// Cart Management
function addToCart(id, name, price) {
    const quantity = parseInt(document.getElementById(`qty-${id}`).value) || 0;
    if (quantity > 0) {
        const existingItem = cart.find(item => item.id === id);
        if (existingItem) {
            existingItem.quantity = quantity;
        } else {
            cart.push({ id, name, price, quantity });
        }
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        showNotification(`${name} added to cart!`);
    } else {
        showNotification('Please select a quantity before adding to cart');
    }
}

function incrementQuantity(id) {
    const input = document.getElementById(`qty-${id}`);
    input.value = parseInt(input.value || 0) + 1;
}

function decrementQuantity(id) {
    const input = document.getElementById(`qty-${id}`);
    const current = parseInt(input.value || 0);
    if (current > 0) {
        input.value = current - 1;
    }
}

function updateCartDisplay() {
    const cartItemsDiv = document.getElementById('cartItems');
    const subtotal = document.getElementById('subtotal');
    const total = document.getElementById('total');

    if (cart.length === 0) {
        cartItemsDiv.innerHTML = '<p style="text-align: center; color: var(--gray-color);">Cart is empty</p>';
        subtotal.textContent = '₹0';
        total.textContent = '₹50';
        return;
    }

    cartItemsDiv.innerHTML = '';
    let cartTotal = 0;

    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        cartTotal += itemTotal;

        const cartItemDiv = document.createElement('div');
        cartItemDiv.className = 'cart-item';
        cartItemDiv.innerHTML = `
            <div class="cart-item-name">${item.name} × ${item.quantity}</div>
            <div class="cart-item-price">₹${itemTotal}</div>
            <button class="cart-remove" onclick="removeFromCart(${item.id})">×</button>
        `;
        cartItemsDiv.appendChild(cartItemDiv);
    });

    subtotal.textContent = '₹' + cartTotal;
    total.textContent = '₹' + (cartTotal + 50);
}

function removeFromCart(id) {
    cart = cart.filter(item => item.id !== id);
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartDisplay();
}

function goToCheckout() {
    if (cart.length === 0) {
        showNotification('Your cart is empty!');
        return;
    }

    // Display checkout summary
    const checkoutSummary = document.getElementById('checkoutSummary');
    const checkoutSubtotal = document.getElementById('checkoutSubtotal');
    const checkoutTotal = document.getElementById('checkoutTotal');

    checkoutSummary.innerHTML = '';
    let cartTotal = 0;

    cart.forEach(item => {
        const itemTotal = item.price * item.quantity;
        cartTotal += itemTotal;

        const itemDiv = document.createElement('div');
        itemDiv.className = 'checkout-item';
        itemDiv.innerHTML = `
            <span>${item.name} × ${item.quantity}</span>
            <span>₹${itemTotal}</span>
        `;
        checkoutSummary.appendChild(itemDiv);
    });

    checkoutSubtotal.textContent = '₹' + cartTotal;
    checkoutTotal.textContent = '₹' + (cartTotal + 50);

    if (currentUser) {
        document.getElementById('checkoutPhone').value = currentUser.phone || '';
    }

    goToPage('checkout');
}

// Initialize checkout form submit handler when DOM is ready
function initCheckoutForm() {
    const checkoutForm = document.getElementById('checkoutForm');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (cart.length === 0) {
                showNotification('Your cart is empty!');
                return;
            }

            const createOrderRequest = {
                deliveryAddress: document.getElementById('deliveryAddress').value,
                phoneNumber: document.getElementById('checkoutPhone').value,
                notes: document.getElementById('specialNotes').value,
                items: cart.map(item => ({
                    menuItemId: item.id,
                    quantity: item.quantity,
                    specialInstructions: ''
                }))
            };

            try {
                const response = await fetch(API_BASE_URL + '/orders/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + authToken
                    },
                    body: JSON.stringify(createOrderRequest)
                });

                if (response.ok) {
                    const order = await response.json();
                    showNotification('Order placed successfully! Order ID: ' + order.id);

                    // Clear cart and go to orders page
                    cart = [];
                    localStorage.removeItem('cart');
                    document.getElementById('checkoutForm').reset();

                    loadUserOrders();
                    goToPage('orders');
                } else {
                    let errorMessage = 'Unknown error';
                    try {
                        const error = await response.json();
                        errorMessage = error.message || error.error || 'Failed to place order';
                    } catch (e) {
                        errorMessage = 'Server error (HTTP ' + response.status + ')';
                    }
                    showNotification('Error placing order: ' + errorMessage);
                    console.error('Order error response:', response.status, response.statusText);
                }
            } catch (error) {
                console.error('Order error:', error);
                showNotification('Failed to place order: ' + error.message);
            }
        });
    }
}

// Orders Management
async function loadUserOrders() {
    try {
        const response = await fetch(API_BASE_URL + '/orders/user/my-orders', {
            headers: { 'Authorization': 'Bearer ' + authToken }
        });

        if (!response.ok) {
            throw new Error('Failed to load orders');
        }

        const orders = await response.json();
        displayOrders(orders);
    } catch (error) {
        console.error('Orders loading error:', error);
        document.getElementById('ordersList').innerHTML = '<div class="error-message">Failed to load orders</div>';
    }
}

function displayOrders(orders) {
    const ordersList = document.getElementById('ordersList');
    ordersList.innerHTML = '';

    if (orders.length === 0) {
        ordersList.innerHTML = '<div class="empty-state"><h3>No orders yet</h3><p>Start by ordering some delicious food!</p></div>';
        return;
    }

    const ordersDiv = document.createElement('div');
    ordersDiv.className = 'orders-list';

    orders.forEach(order => {
        const statusClass = order.status.toLowerCase().replace('_', '-');
        const orderCard = document.createElement('div');
        orderCard.className = 'order-card';

        const itemsList = order.items.map(item => `${item.menuItemName} × ${item.quantity}`).join(', ');

        orderCard.innerHTML = `
            <div class="order-header">
                <div class="order-id">Order #${order.id}</div>
                <div class="order-status ${statusClass}">${order.status}</div>
            </div>
            <div class="order-items">
                <strong>Items:</strong> ${itemsList}
            </div>
            <div class="order-total">
                Total: ₹${order.totalAmount + order.deliveryFee}
            </div>
            <p style="font-size: 0.9rem; color: var(--gray-color);">
                Delivery: ${order.deliveryAddress}
            </p>
            <div class="order-actions">
                <button class="track-btn" onclick="trackOrder(${order.id})">Track Order</button>
                ${order.status === 'PENDING' ? `<button class="cancel-btn" onclick="cancelOrder(${order.id})">Cancel</button>` : ''}
            </div>
        `;

        ordersDiv.appendChild(orderCard);
    });

    ordersList.appendChild(ordersDiv);
}

function trackOrder(orderId) {
    fetchOrderTracking(orderId);
    goToPage('tracking');
}

async function fetchOrderTracking(orderId) {
    try {
        const response = await fetch(API_BASE_URL + `/tracking/${orderId}`, {
            headers: { 'Authorization': 'Bearer ' + authToken }
        });

        if (response.ok) {
            const tracking = await response.json();
            displayTracking(tracking);
        } else {
            showNotification('Failed to load tracking information');
        }
    } catch (error) {
        console.error('Tracking error:', error);
    }
}

function displayTracking(tracking) {
    const statusSteps = {
        'PENDING': 1,
        'CONFIRMED': 1,
        'PREPARING': 2,
        'OUT_FOR_DELIVERY': 3,
        'DELIVERED': 4,
        'CANCELLED': 0
    };

    const currentStep = statusSteps[tracking.currentStatus] || 0;

    for (let i = 1; i <= 4; i++) {
        const stepElement = document.getElementById(`step${i}`);
        if (i < currentStep) {
            stepElement.classList.add('completed');
            stepElement.classList.remove('active');
        } else if (i === currentStep) {
            stepElement.classList.add('active');
            stepElement.classList.remove('completed');
        } else {
            stepElement.classList.remove('active', 'completed');
        }
    }

    // Update tracking info
    const deliveryPersonInfo = document.getElementById('deliveryPersonInfo');
    if (tracking.deliveryPersonName) {
        deliveryPersonInfo.innerHTML = `
            <strong>${tracking.deliveryPersonName}</strong><br>
            Phone: ${tracking.deliveryPersonPhone}<br>
            Estimated delivery: ${tracking.estimatedDeliveryTime} minutes
        `;
    } else {
        deliveryPersonInfo.textContent = 'Delivery person will be assigned soon';
    }
}

async function cancelOrder(orderId) {
    if (confirm('Are you sure you want to cancel this order?')) {
        try {
            const response = await fetch(API_BASE_URL + `/orders/${orderId}/cancel`, {
                method: 'PUT',
                headers: { 'Authorization': 'Bearer ' + authToken }
            });

            if (response.ok) {
                showNotification('Order cancelled successfully');
                loadUserOrders();
            } else {
                showNotification('Failed to cancel order');
            }
        } catch (error) {
            console.error('Cancel error:', error);
        }
    }
}

// Search and Filter
// Initialize page on load
document.addEventListener('DOMContentLoaded', () => {
    initAuthForm(); // Initialize auth form submit handler
    initCheckoutForm(); // Initialize checkout form submit handler
    
    updateLoadingStatus('Loading ZippyRoute...');
    
    setTimeout(() => {
        updateLoadingStatus('Connecting to servers...');
    }, 800);

    setTimeout(() => {
        updateLoadingStatus('Preparing your meal...');
    }, 1600);

    setTimeout(() => {
        updateLoadingStatus('System Online!');
        
        // Hide loading screen and show app
        setTimeout(() => {
            hideLoadingScreen();
            
            if (authToken && currentUser) {
                goToPage('menu');
            } else {
                goToPage('home');
            }
        }, 500);
    }, 2400);
});

document.getElementById('searchInput')?.addEventListener('input', (e) => {
    const searchTerm = e.target.value.toLowerCase();
    const filtered = menuItems.filter(item =>
        item.name.toLowerCase().includes(searchTerm) ||
        (item.description && item.description.toLowerCase().includes(searchTerm))
    );
    displayMenu(filtered);
});

document.getElementById('categoryFilter').addEventListener('change', (e) => {
    const category = e.target.value;
    if (category) {
        const filtered = menuItems.filter(item => item.category === category);
        displayMenu(filtered);
    } else {
        displayMenu(menuItems);
    }
});

// Initialize
window.addEventListener('load', () => {
    if (authToken && currentUser) {
        goToPage('menu');
    } else {
        goToPage('auth');
    }
});
