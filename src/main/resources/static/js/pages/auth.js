import { api } from "../core/api.js";
import { els } from "../core/dom.js";
import { saveSession } from "../core/state.js";
import { errorText, normalizeRole } from "../core/utils.js";
import { registerFields, field } from "../config/fields.js";
import { fieldsHtml, formValues } from "../ui/forms.js";
import { setLoading, toast } from "../ui/feedback.js";

export function initAuth(enterApp) {
    els.registerFields.innerHTML = fieldsHtml(registerFields);
    els.loginForm.addEventListener("submit", function (event) { login(event, enterApp); });
    els.registerForm.addEventListener("submit", registerReader);
    document.getElementById("showRegisterButton").addEventListener("click", showRegister);
    document.getElementById("backToLoginButton").addEventListener("click", showLogin);
}

async function login(event, enterApp) {
    event.preventDefault();
    const button = els.loginForm.querySelector("button[type=submit]");
    setLoading(button, true);
    try {
        const payload = formValues(els.loginForm, [
            field("username", "Tên đăng nhập"),
            field("password", "Mật khẩu")
        ]);
        const result = await api("/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        if (!result.idUser || !result.role) {
            throw new Error("Thông tin đăng nhập không hợp lệ.");
        }
        saveSession({
            username: result.username || payload.username,
            email: result.email || "",
            role: normalizeRole(result.role),
            idUser: result.idUser,
            avatar: result.avatar || ""
        });
        toast("Đăng nhập thành công.", "success");
        enterApp();
    } catch (err) {
        toast(errorText(err), "error");
    } finally {
        setLoading(button, false);
    }
}

async function registerReader(event) {
    event.preventDefault();
    const button = els.registerForm.querySelector("button[type=submit]");
    setLoading(button, true);
    try {
        const payload = formValues(els.registerForm, registerFields);
        const response = await api("/api/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        toast(response.message || "Tài khoản đã được tạo.", "success");
        els.registerForm.reset();
        showLogin();
    } catch (err) {
        toast(errorText(err), "error");
    } finally {
        setLoading(button, false);
    }
}

function showRegister() {
    els.loginForm.classList.add("is-hidden");
    els.registerForm.classList.remove("is-hidden");
}

function showLogin() {
    els.registerForm.classList.add("is-hidden");
    els.loginForm.classList.remove("is-hidden");
}
