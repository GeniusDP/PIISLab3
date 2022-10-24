package org.example.models.nega;

import static java.lang.Math.max;
import static org.example.models.algorithms.astar.Algorithm.INF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.example.models.Direction;
import org.example.models.Matrix;

public class NegaScout {

  private static final int MAX_DEPTH = 6;

  private static int cnt;

  private NegaScout() {
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

  private static int perform(State state, int alpha, int beta) {
    cnt++;
    int b, t;
    if (state.isTerminal()) {
      return state.getMetrics();
    }
    List<State> actions = state.getActions().stream().toList();
    b = beta;
    for (int i = 0; i < actions.size(); i++){
      State child = actions.get(i);
      t = -perform(child, -b, -alpha);
      if( (t > alpha) && (t < beta) && (i > 1) ){
        t = -perform(child, -beta, -alpha);
      }
      alpha = max(alpha, t);
      if(alpha >= beta){
        return alpha;
      }
      b = alpha + 1;
    }
    return alpha;
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

