import React from 'react'
import { Tabs } from 'expo-router'
import TabBar from "@/components/tab-bar";
import "../global.css";

const _layout = () => {
    return (
        <Tabs
            screenOptions={{
                headerShown: false,
                sceneStyle: {
                    backgroundColor: '#1c1c1c'
                }
            }}
            tabBar={props=> <TabBar {...props} />}
        >
            <Tabs.Screen
                name="index"
                options={{
                    title: "Home"
                }}
            />
            <Tabs.Screen
                name="settings"
                options={{
                    title: "Settings"
                }}
            />
        </Tabs>
    )
}

export default _layout