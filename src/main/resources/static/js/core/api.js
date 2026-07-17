import { errorText, tryJson } from "./utils.js";
import { toast } from "../ui/feedback.js";

export async function api(path, options) {
    const response = await fetch(path, options || {});
    const text = await response.text();
    const data = text ? tryJson(text) : {};
    if (!response.ok) {
        const error = new Error(data.message || "Yêu cầu không thành công.");
        error.status = response.status;
        error.data = data;
        throw error;
    }
    return data;
}

export async function optionalApi(path, fallback) {
    try {
        return await api(path);
    } catch (err) {
        return fallback;
    }
}

export async function submitJson(method, path, payload, message) {
    const options = { method: method };
    if (payload !== null && payload !== undefined) {
        options.headers = { "Content-Type": "application/json" };
        options.body = JSON.stringify(payload);
    }
    const result = await api(path, options);
    toast(result.message || message || "Đã hoàn tất.", "success");
    return result;
}

export function reportError(err) {
    toast(errorText(err), "error");
}
