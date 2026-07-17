import { SESSION_KEY } from "./constants.js";

export const state = {
    session: readSession(),
    page: "dashboard",
    pageNo: {},
    rows: [],
    caches: {}
};

export function saveSession(session) {
    state.session = session;
    localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

export function clearSession() {
    localStorage.removeItem(SESSION_KEY);
    state.session = null;
    state.page = "dashboard";
    state.pageNo = {};
    state.rows = [];
    state.caches = {};
}

function readSession() {
    try {
        return JSON.parse(localStorage.getItem(SESSION_KEY) || "null");
    } catch (err) {
        return null;
    }
}
