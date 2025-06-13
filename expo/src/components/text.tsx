import {TextProps, Text} from "react-native";
import {cn} from "@/utils/utils";

interface CustomTextProps extends TextProps {}

const CustomText = (props: CustomTextProps) => {
    return <Text {...props} className={cn('text-white', props.className)}/>;
}

export default CustomText;