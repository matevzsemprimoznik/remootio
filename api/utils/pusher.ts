const Pusher = require('pusher');

export const pusher = new Pusher({
    appId: process.env.PUSHER_APP_ID,
    key: process.env.PUSHER_KEY,
    cluster: process.env.PUSHER_CLUSTER,
    secret: process.env.PUSHER_SECRET,
})