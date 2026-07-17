import { cacheDom, els } from "./core/dom.js";
import { clearSession, state } from "./core/state.js";
import { defaultPageForPath } from "./core/utils.js";
import { initAuth } from "./pages/auth.js";
import { renderPage } from "./router.js";
import { closeDrawer } from "./ui/drawer.js";
import { renderShell } from "./ui/shell.js";

document.addEventListener("DOMContentLoaded", init);

function init() {
    cacheDom();
    initAuth(enterApp);

    document.getElementById("logoutButton").addEventListener("click", logout);
    document.getElementById("refreshButton").addEventListener("click", function () { navigate(false); });
    document.getElementById("drawerCloseButton").addEventListener("click", closeDrawer);
    els.drawer.addEventListener("click", function (event) {
        if (event.target === els.drawer) {
            closeDrawer();
        }
    });
    window.addEventListener("ktt:navigate", function (event) {
        navigate(event.detail);
    });

    if (state.session) {
        enterApp();
    } else {
        showAuth();
    }
}

function enterApp() {
    els.loginScreen.classList.add("is-hidden");
    els.productShell.classList.remove("is-hidden");
    state.page = defaultPageForPath(state.session.role);
    renderShell(navigate);
    navigate(false);

    const target = state.session.role === "USER" ? "/user" : state.session.role === "ADMIN" ? "/admin" : "/staff";
    if (location.pathname !== target) {
        history.replaceState(null, "", target);
    }
}

function showAuth() {
    els.productShell.classList.add("is-hidden");
    els.loginScreen.classList.remove("is-hidden");
    if (location.pathname !== "/login") {
        history.replaceState(null, "", "/login");
    }
}

function logout() {
    clearSession();
    closeDrawer();
    showAuth();
}

function navigate(page) {
    if (typeof page === "string") {
        state.page = page;
    }
    renderShell(navigate);
    renderPage({
        onAuthRequired: showAuth,
        refreshShell: function () { renderShell(navigate); }
    });
}
