// toast.js - Custom Toast Notification and Modal System (No native alerts)

(function () {
    // Create toast container if not present
    function getToastContainer() {
        let container = document.getElementById("toast-container");
        if (!container) {
            container = document.createElement("div");
            container.id = "toast-container";
            container.className = "toast-container";
            document.body.appendChild(container);
        }
        return container;
    }

    // Main Toast function
    window.showToast = function (message, type = "info", title = null, duration = 3500) {
        const container = getToastContainer();

        const toast = document.createElement("div");
        toast.className = `toast toast-${type}`;

        const icons = {
            success: '✓',
            error: '✕',
            warning: '⚠',
            info: 'ℹ'
        };

        const defaultTitles = {
            success: 'Success',
            error: 'Error',
            warning: 'Warning',
            info: 'Notice'
        };

        const toastTitle = title || defaultTitles[type] || 'Notice';

        toast.innerHTML = `
            <div class="toast-icon">${icons[type] || 'ℹ'}</div>
            <div class="toast-content">
                <div class="toast-title">${toastTitle}</div>
                <div class="toast-message">${message}</div>
            </div>
            <button class="toast-close" onclick="this.parentElement.remove()">&times;</button>
        `;

        container.appendChild(toast);

        // Auto remove
        setTimeout(() => {
            if (toast.parentElement) {
                toast.classList.add("toast-hiding");
                toast.addEventListener("animationend", () => {
                    if (toast.parentElement) toast.remove();
                });
            }
        }, duration);
    };

    // Replace native browser alert with toast
    window.alert = function (message) {
        window.showToast(message, "info", "Notification");
    };

    // Custom confirm dialog replacement
    window.showConfirm = function (message, title = "Confirm Action") {
        return new Promise((resolve) => {
            const overlay = document.createElement("div");
            overlay.className = "modal-overlay";

            overlay.innerHTML = `
                <div class="custom-modal card">
                    <h3>${title}</h3>
                    <p style="color: #ccc; margin-top: 0.5rem; margin-bottom: 1.5rem;">${message}</p>
                    <div class="d-flex gap-2 justify-end">
                        <button class="btn btn-outline" id="modalCancelBtn">Cancel</button>
                        <button class="btn btn-danger" id="modalConfirmBtn">Confirm</button>
                    </div>
                </div>
            `;

            document.body.appendChild(overlay);

            document.getElementById("modalConfirmBtn").addEventListener("click", () => {
                overlay.remove();
                resolve(true);
            });

            document.getElementById("modalCancelBtn").addEventListener("click", () => {
                overlay.remove();
                resolve(false);
            });
        });
    };
})();
