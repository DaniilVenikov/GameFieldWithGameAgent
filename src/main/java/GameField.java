import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameField {
    public static void main(String[] args) {
        int N = 4; // количество строк
        int M = 4; // количество столбцов
        int K = 3; // количество ловушек

        List<List<Map<String, Boolean>>> playMap = new ArrayList<>();

        // Создаём пустое игровое поле
        for (int i = 0; i < N; i++) {
            List<Map<String, Boolean>> row = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                Map<String, Boolean> cell = new HashMap<>();
                cell.put("trap", false);
                cell.put("gold", false);
                cell.put("danger", false);
                cell.put("visited", false);
                cell.put("isWarning", false);
                row.add(cell);
            }
            playMap.add(row);
        }

        Random random = new Random();

        // Генерируем случайные координаты для ловушек
        List<int[]> trapElems = new ArrayList<>();
        for (int k = 0; k < K; k++) {
            int[] trap = new int[]{random.nextInt(N), random.nextInt(M)};
            trapElems.add(trap);
        }

        // Генерируем случайные координаты для золота (в данном случае 1 золото)
        int[] goldElem = new int[]{random.nextInt(N), random.nextInt(M)};

        // Заполняем поле значениями для ловушек и золота
        for (int k = 0; k < K; k++) {
            int[] trap = trapElems.get(k);
            playMap.get(trap[0]).get(trap[1]).put("trap", true);
            // случай, когда ловушка находиться сразу возле поля (0;0), например с координатами (0;1) или (1;0)
            playMap.get(trap[0]).get(trap[1]).put("isWarning", true);

            // Помечаем клетки как опасные
            if (trap[0] + 1 < N) {
                playMap.get(trap[0] + 1).get(trap[1]).put("danger", true);
            }
            if (trap[0] - 1 >= 0) {
                playMap.get(trap[0] - 1).get(trap[1]).put("danger", true);
            }
            if (trap[1] + 1 < M) {
                playMap.get(trap[0]).get(trap[1] + 1).put("danger", true);
            }
            if (trap[1] - 1 >= 0) {
                playMap.get(trap[0]).get(trap[1] - 1).put("danger", true);
            }
        }
        playMap.get(0).get(0).put("trap", false);

        //надо нам, чтобы клетка с золотом могла быть помечена как danger или нет?
        //playMap.get(goldElem[0]).get(goldElem[1]).put("danger", false);

        playMap.get(0).get(0).put("danger", false);


        playMap.get(goldElem[0]).get(goldElem[1]).put("gold", true);
        playMap.get(goldElem[0]).get(goldElem[1]).put("trap", false);

        // Вывод игрового поля
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.println("Cell (" + (i + 1) + ", " + (j + 1) + "): trap=" + playMap.get(i).get(j).get("trap") +
                        ", gold=" + playMap.get(i).get(j).get("gold") +
                        ", danger=" + playMap.get(i).get(j).get("danger") +
                        ", visited=" + playMap.get(i).get(j).get("visited"));
            }
        }

        Agent agent = new Agent(playMap);
        agent.searchGold(0,0);


        JSONObject gameFieldJson = new JSONObject();
        JSONArray field = new JSONArray();

        for (int i = 0; i < N; i++) {
            JSONArray row = new JSONArray();
            for (int j = 0; j < M; j++) {
                Map<String, Boolean> cell = playMap.get(i).get(j);
                JSONObject cellJson = new JSONObject(cell);
                row.put(cellJson);
            }
            field.put(row);
        }

        gameFieldJson.put("field", field);

        try (FileWriter file = new FileWriter("gameField.json")) {
            file.write(gameFieldJson.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
