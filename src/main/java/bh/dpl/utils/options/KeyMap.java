package bh.dpl.utils.options;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KevinLeft
 * @since 2024 - Aug - 14
 */
public class KeyMap {
    // Mojang的死人方法 现在只能用映射过去了
    public static Map<Character, Integer> keyMap = new HashMap<>();
    public static void load(){
        // 字母 a-z
        keyMap.put('a', GLFW.GLFW_KEY_A);
        keyMap.put('b', GLFW.GLFW_KEY_B);
        keyMap.put('c', GLFW.GLFW_KEY_C);
        keyMap.put('d', GLFW.GLFW_KEY_D);
        keyMap.put('e', GLFW.GLFW_KEY_E);
        keyMap.put('f', GLFW.GLFW_KEY_F);
        keyMap.put('g', GLFW.GLFW_KEY_G);
        keyMap.put('h', GLFW.GLFW_KEY_H);
        keyMap.put('i', GLFW.GLFW_KEY_I);
        keyMap.put('j', GLFW.GLFW_KEY_J);
        keyMap.put('k', GLFW.GLFW_KEY_K);
        keyMap.put('l', GLFW.GLFW_KEY_L);
        keyMap.put('m', GLFW.GLFW_KEY_M);
        keyMap.put('n', GLFW.GLFW_KEY_N);
        keyMap.put('o', GLFW.GLFW_KEY_O);
        keyMap.put('p', GLFW.GLFW_KEY_P);
        keyMap.put('q', GLFW.GLFW_KEY_Q);
        keyMap.put('r', GLFW.GLFW_KEY_R);
        keyMap.put('s', GLFW.GLFW_KEY_S);
        keyMap.put('t', GLFW.GLFW_KEY_T);
        keyMap.put('u', GLFW.GLFW_KEY_U);
        keyMap.put('v', GLFW.GLFW_KEY_V);
        keyMap.put('w', GLFW.GLFW_KEY_W);
        keyMap.put('x', GLFW.GLFW_KEY_X);
        keyMap.put('y', GLFW.GLFW_KEY_Y);
        keyMap.put('z', GLFW.GLFW_KEY_Z);

        // 数字 1-9
        keyMap.put('1', GLFW.GLFW_KEY_1);
        keyMap.put('2', GLFW.GLFW_KEY_2);
        keyMap.put('3', GLFW.GLFW_KEY_3);
        keyMap.put('4', GLFW.GLFW_KEY_4);
        keyMap.put('5', GLFW.GLFW_KEY_5);
        keyMap.put('6', GLFW.GLFW_KEY_6);
        keyMap.put('7', GLFW.GLFW_KEY_7);
        keyMap.put('8', GLFW.GLFW_KEY_8);
        keyMap.put('9', GLFW.GLFW_KEY_9);
        // 注意 '0' 键
        keyMap.put('0', GLFW.GLFW_KEY_0);
    }
}
