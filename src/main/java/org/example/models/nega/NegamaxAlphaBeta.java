package org.example.models.nega;

import static java.lang.Math.max;
import static org.example.models.algorithms.astar.Algorithm.INF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.example.models.Direction;
import org.example.models.Matrix;

public class NegamaxAlphaBeta {

  private static final int MAX_DEPTH = 6;

  private static int cnt;

  private NegamaxAlphaBeta() {
  }

  public static State makeDecision(State state, int a, int b) {
    cnt = 0;
    List<State> actions = state.getActions().stream().toList();
    int value = -INF;
    State result = state;
    for (var s: actions){
      int current = perform(s, a, b);
      if(current > value){
        value = current;
        result = s;
      }
    }
    return result;
  }

  private static int perform(State state, int a, int b) {
    cnt++;
    if (state.isTerminal()) {
      return state.getMetrics();
    }
    int value = -INF;
    for (var child: state.getActions()){
      value = max(value, -perform(child, -b, -a));
      a = max(a, value);
      if(a >= b){
        break;
      }
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
