import { AntDesign, Feather } from "@expo/vector-icons";

export const icons = {
    index: (props: any)=> <AntDesign name="home" size={26} {...props} />,
    settings: (props: any)=> <Feather name="settings" size={26} {...props} />,
} as const