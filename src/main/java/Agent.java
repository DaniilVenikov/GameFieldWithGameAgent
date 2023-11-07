import java.util.List;
import java.util.Map;

public class Agent {

    private List<List<Map<String, Boolean>>> playMap;
    private int N;
    private int M;
    private int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};


    public Agent(List<List<Map<String, Boolean>>> playMap) {
        this.playMap = playMap;
        this.N = playMap.size();
        this.M = playMap.get(0).size();
    }

    public void searchGold(int x, int y) {
        if (x < 0 || x >= M || y < 0 || y >= N || playMap.get(x).get(y).get("visited")) {
            return; // Клетка посещена или находится за пределами поля
        }

        // Помечаем текущую клетку как посещенную
        playMap.get(y).get(x).put("visited", true);

        // Если мы нашли золото, выходим из рекурсии
        if (playMap.get(y).get(x).get("gold")) {
            System.out.println("Найдено золото в клетке (" + (x + 1) + ", " + (y + 1) + ")");
            return;
        }

        // Если клетка помечена как danger, то выставляем непосещённые поля вокруг как опасные
        if (playMap.get(y).get(x).get("danger")) {
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (isValidMove(newX, newY)) {
                     playMap.get(newY).get(newX).put("isWarning", true);
                }
            }
        }

        // Проверяем, есть ли безопасные ходы
        boolean hasSafeMoves = false;
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (isValidMove(newX, newY) && !playMap.get(newY).get(newX).get("isWarning")) {
                hasSafeMoves = true;
                break;
            }
        }

        // Если есть безопасные ходы, двигаемся в одном из них
        if (hasSafeMoves) {
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (isValidMove(newX, newY) && !playMap.get(newY).get(newX).get("isWarning")) {
                    searchGold(newX, newY);
                    return;
                }
            }
        } else {
            // Если нет безопасных ходов, выбираем любое направление
            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];
                if (isValidMove(newX, newY)) {
                    searchGold(newX, newY);
                    return;
                }
            }
        }
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < M && y >= 0 && y < N && !playMap.get(y).get(x).get("visited");
    }

}
