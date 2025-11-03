// js/auth.js
class AuthService {
    constructor() {
        this.currentUser = null;
    }

    // Store user session
    setSession(user) {
        this.currentUser = user;
        localStorage.setItem('currentUser', JSON.stringify(user));
    }

    // Get current user
    getCurrentUser() {
        if (!this.currentUser) {
            const stored = localStorage.getItem('currentUser');
            if (stored) {
                this.currentUser = JSON.parse(stored);
            }
        }
        return this.currentUser;
    }

    // Check if user is logged in
    isLoggedIn() {
        return this.getCurrentUser() !== null;
    }

    // Logout
    logout() {
        this.currentUser = null;
        localStorage.removeItem('currentUser');
        window.location.href = 'login.html';
    }

    // Get user role
    getUserRole() {
        const user = this.getCurrentUser();
        return user ? user.role : null;
    }
}

// Create global auth instance
window.authService = new AuthService();