import {pusher} from "../utils/pusher";

enum Action {
    OPEN = "OPEN",
    CLOSE = "CLOSE",
    KEEP_OPENED = "KEEP_OPENED"
}

export async function POST(request: Request) {
    try {
        const body = await request.json();
        const action = body.action as Action | undefined;
        if (!action || !Object.values(Action).includes(action)) {
            return new Response(JSON.stringify({ message: "Invalid action" }), { status: 400 });
        }
        await pusher.trigger("actions", action, {})
    }
    catch (e) {
        console.log(e);
        return new Response(JSON.stringify({ message: 'Internal server error' }), { status: 500 });
    }
    return new Response(JSON.stringify({ message: 'Success' }), { status: 200 });
}
