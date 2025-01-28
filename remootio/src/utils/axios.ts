import axios from "axios";
import {env} from "@/utils/env";

export const actionInstance = axios.create({
    baseURL: env.EXPO_PUBLIC_ACTION_API_URL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    }
})