package org.example.models.nega;

import static java.lang.Math.max;
import static org.example.models.algorithms.astar.Algorithm.INF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.example.models.Direction;
import org.example.models.Matrix;

public class Negamax {

  private static final int MAX_DEPTH = 12;

  private static int cnt;

  private Negamax() {
  }

  public static State makeDecision(State state) {
    cnt = 0;
    return state.getActions().stream().max(Comparator.comparing(Negamax::perform)).get();
  }

  private static int perform(State state) {
    cnt++;
    if (state.isTerminal()) {
      return state.getMetrics();
    }
    int value = -INF;
    for (var child: state.getActions()){
      value = max(value, -perform(child));
    }
    return value;
  }

  public record State(Matrix state, int color) {

    Collection<State> getActions() {
      List<State> actions = new ArrayList<>();
      if (state.playerCanMove(Direction.UP)) {
        Matrix moved = state.playerMove(Direction.UP);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.DOWN)) {
        Matrix moved = state.playerMove(Direction.DOWN);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.LEFT)) {
        Matrix moved = state.playerMove(Direction.LEFT);
        actions.add(new State(moved, -color));
      }
      if (state.playerCanMove(Direction.RIGHT)) {
        Matrix moved = state.playerMove(Direction.RIGHT);
        actions.add(new State(moved, -color));
      }
      return actions;
    }

    boolean isTerminal() {
      return cnt > MAX_DEPTH || state.isFinish();
    }

    int getMetrics() {
      return color * state.getMetrics();
    }
  }

}
