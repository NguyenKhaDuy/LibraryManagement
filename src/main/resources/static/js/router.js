import { els } from "./core/dom.js";
import { state } from "./core/state.js";
import { allowedPagesForRole, firstPageForRole } from "./config/navigation.js";
import { resourceConfigs, renderResource, renderSetup } from "./pages/resources.js";
import { renderBooks } from "./pages/books.js";
import { renderCart, renderMyLoans } from "./pages/user.js";
import { renderCirculation } from "./pages/circulation.js";
import { renderDashboard } from "./pages/dashboard.js";
import { renderImports } from "./pages/imports.js";
import { renderProfile } from "./pages/profile.js";
import { closeDrawer } from "./ui/drawer.js";

export async function renderPage(context) {
    const options = context || {};
    if (!state.session) {
        if (options.onAuthRequired) options.onAuthRequired();
        return;
    }

    closeDrawer();
    const allowed = allowedPagesForRole(state.session.role);
    if (allowed.indexOf(state.page) < 0) {
        state.page = firstPageForRole(state.session.role);
        if (options.refreshShell) options.refreshShell();
    }

    const page = state.page;
    if (page === "dashboard") {
        await renderDashboard();
    } else if (page === "catalog") {
        await renderBooks(true);
    } else if (page === "books") {
        await renderBooks(false);
    } else if (page === "cart") {
        await renderCart();
    } else if (page === "myLoans") {
        await renderMyLoans();
    } else if (page === "circulation") {
        await renderCirculation();
    } else if (page === "imports") {
        await renderImports();
    } else if (page === "setup") {
        renderSetup();
    } else if (page === "profile") {
        await renderProfile();
    } else if (resourceConfigs[page]) {
        await renderResource(resourceConfigs[page]);
    } else {
        state.page = firstPageForRole(state.session.role);
        if (options.refreshShell) options.refreshShell();
        await renderDashboard();
    }

    if (els.pageRoot) {
        els.pageRoot.focus({ preventScroll: true });
    }
}
