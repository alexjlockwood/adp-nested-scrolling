package design.shapeshifter.nestedscrolling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * A custom {@link NestedScrollView} that customizes the sample app's
 * nested scrolling behavior.
 */
class CustomNestedScrollView2 extends NestedScrollView2 {

  public CustomNestedScrollView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onNestedPreScroll(
      @NonNull View target, int dx, int dy, int[] consumed, int type) {
    final NestedScrollView nsv = this;
    final RecyclerView rv = (RecyclerView) target;
    if ((dy < 0 && isScrolledToTop(rv))
        || (dy > 0 && !isScrolledToBottom(nsv))) {
      // The NestedScrollView should steal the scroll event away from the
      // RecyclerView in one of two cases: (1) if the user is scrolling their
      // finger down and the RecyclerView is scrolled to the top, or (2) if
      // the user is scrolling their finger up and the NestedScrollView is
      // not scrolled to the bottom.
      scrollBy(0, dy);
      consumed[1] = dy;
      return;
    }
    super.onNestedPreScroll(target, dx, dy, consumed, type);
  }

  @Override
  public boolean onNestedPreFling(
      @NonNull View target, float velX, float velY) {
    final NestedScrollView nsv = this;
    final RecyclerView rv = (RecyclerView) target;
    if ((velY < 0 && isScrolledToTop(rv))
        || (velY > 0 && !isScrolledToBottom(nsv))) {
      // The NestedScrollView should steal the fling event away from the
      // RecyclerView in one of two cases: (1) if the user is flinging their
      // finger down and the RecyclerView is scrolled to the top, or (2) if
      // the user is flinging their finger up and the NestedScrollView is
      // not scrolled to the bottom.
      fling((int) velY);
      return true;
    }
    return super.onNestedPreFling(target, velX, velY);
  }

  /**
   * Returns true iff the {@link NestedScrollView} is scrolled to the bottom
   * of its viewport.
   */
  private static boolean isScrolledToBottom(NestedScrollView nsv) {
    return !nsv.canScrollVertically(1);
  }

  /**
   * Returns true iff the vertical {@link RecyclerView} is scrolled to the
   * top (i.e. its first item is completely visible).
   */
  private static boolean isScrolledToTop(RecyclerView rv) {
    final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
    return lm.findFirstVisibleItemPosition() == 0
        && lm.findViewByPosition(0).getTop() == 0;
  }
}
