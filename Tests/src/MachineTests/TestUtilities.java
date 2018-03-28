package MachineTests;

import java.util.List;

public class TestUtilities {

    public static void printList(List<?> res) {
        for(int i = 0; i < res.size(); i++)
        {
            System.out.println(res.get(i));
        }

        System.out.println();
    }
}
