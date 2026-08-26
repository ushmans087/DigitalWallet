// auth.js - Shared authentication logic & Role management

function getToken() {
    return localStorage.getItem("jwt");
}

function setToken(token) {
    localStorage.setItem("jwt", token);
}

function getUserEmail() {
    return localStorage.getItem("userEmail");
}

function setUserEmail(email) {
    localStorage.setItem("userEmail", email);
}

function getUserRole() {
    return localStorage.getItem("userRole") || "USER";
}

function setUserRole(role) {
    localStorage.setItem("userRole", role);
}

function isValidator() {
    const role = getUserRole();
    return role === "VALIDATOR" || role === "TRANSACTION_VALIDATOR";
}

// Utility to parse role from JWT token string if present
function parseJwtRole(token) {
    if (!token) return null;
    try {
        const parts = token.split('.');
        if (parts.length === 3) {
            const payload = JSON.parse(atob(parts[1]));
            if (payload.role) return payload.role.toUpperCase();
            if (payload.roles && Array.isArray(payload.roles)) return payload.roles[0].toUpperCase();
            if (payload.sub && (payload.sub.includes('validator') || payload.sub.includes('admin'))) return "VALIDATOR";
        }
    } catch (e) {
        console.warn("Could not parse JWT token payload", e);
    }
    return null;
}

function logout() {
    localStorage.removeItem("jwt");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");
    window.location.href = "login.html";
}

function checkAuth() {
    const token = getToken();
    if (!token) {
        window.location.href = "login.html";
        return;
    }

    const currentPath = window.location.pathname.split("/").pop() || "index.html";

    // Guard routes based on role
    if (isValidator()) {
        if (currentPath !== "validator.html") {
            window.location.href = "validator.html";
        }
    } else {
        if (currentPath === "validator.html") {
            window.location.href = "dashboard.html";
        }
    }
}

// Utility function for making authenticated API calls
async function apiCall(endpoint, options = {}) {
    const url = `${CONFIG.API_BASE_URL}${endpoint}`;
    
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = {
        ...options,
        headers
    };

    try {
        const response = await fetch(url, config);
        
        // Handle unauthorized
        if (response.status === 401 || response.status === 403) {
            logout();
            throw new Error("Unauthorized");
        }
        
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.indexOf("application/json") !== -1) {
            const data = await response.json();
            return { ok: response.ok, status: response.status, data };
        } else {
            const text = await response.text();
            return { ok: response.ok, status: response.status, data: text };
        }
        
    } catch (error) {
        console.error("API Call Error:", error);
        throw error;
    }
}

// Sidebar logic to configure nav links and active state based on role
document.addEventListener("DOMContentLoaded", () => {
    const currentPath = window.location.pathname.split("/").pop() || "index.html";
    const navLinksContainer = document.querySelector(".nav-links");

    // If logged in as Transaction Validator, update sidebar to single page link
    if (navLinksContainer && isValidator()) {
        navLinksContainer.innerHTML = `
            <li class="nav-item">
                <a href="validator.html" class="nav-link ${currentPath === 'validator.html' ? 'active' : ''}">
                    Bank Transactions
                </a>
            </li>
        `;
    } else if (navLinksContainer) {
        const navLinks = document.querySelectorAll(".nav-link");
        navLinks.forEach(link => {
            const href = link.getAttribute("href");
            if (href === currentPath) {
                link.classList.add("active");
            }
        });
    }

    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }
});
