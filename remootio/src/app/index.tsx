import {View, TouchableOpacity} from "react-native";
import CustomText from "@/components/text";
import {env} from "@/utils/env";
import {actionInstance} from "@/utils/axios";

enum Action {
    OPEN = "OPEN",
    CLOSE = "CLOSE",
    KEEP_OPENED = "KEEP_OPENED"
}

export default function Home() {
    const sendAction = async (action: Action) => {
        try {
            await actionInstance.post(env.EXPO_PUBLIC_ACTION_API_URL, { action })
        }
        catch (e) {
            console.log(e);
        }
    }

    return (
        <View className='bg-primary flex-1'>
            <CustomText className="text-3xl font-bold mb-10 text-center mt-5">Remootio</CustomText>
            <View className="flex flex-col justify-center flex-1 px-10 mb-24 gap-8">
                <TouchableOpacity onPress={() => sendAction(Action.OPEN)} className="m-2 bg-[#3a86ff] h-52 flex justify-center items-center rounded-2xl">
                    <CustomText className="text-center text-lg">Open / Close Gates</CustomText>
                </TouchableOpacity>

                <TouchableOpacity onPress={() => sendAction(Action.KEEP_OPENED)} className="m-2 bg-[#7ae582]  h-52 flex justify-center items-center rounded-2xl">
                    <CustomText className="text-center text-black text-lg">Keep Gates Opened</CustomText>
                </TouchableOpacity>
            </View>
        </View>
    )
}