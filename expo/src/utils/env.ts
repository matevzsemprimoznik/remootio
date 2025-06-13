import z from "zod";

const envSchema = z.object({
    EXPO_PUBLIC_ACTION_API_URL: z.string().url()
});

export const env = envSchema.parse(process.env);