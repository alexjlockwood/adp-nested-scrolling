package alexjlockwood.nestedscrolling;

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
public class CustomNestedScrollView2 extends NestedScrollView2 {

  public CustomNestedScrollView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
    final RecyclerView rv = (RecyclerView) target;
    if ((dy < 0 && isRvScrolledToTop(rv)) || (dy > 0 && !isNsvScrolledToBottom(this))) {
      // The NestedScrollView should steal the scroll event away from the
      // RecyclerView if: (1) the user is scrolling their finger down and the
      // RecyclerView is scrolled to the top of its content, or (2) the user
      // is scrolling their finger up and the NestedScrollView is not scrolled
      // to the bottom of its content.
      scrollBy(0, dy);
      consumed[1] = dy;
      return;
    }
    super.onNestedPreScroll(target, dx, dy, consumed, type);
  }

  /**
   * Returns true iff the {@link NestedScrollView} is scrolled to the bottom
   * of its content (i.e. the card is completely expanded).
   */
  private static boolean isNsvScrolledToBottom(NestedScrollView nsv) {
    return !nsv.canScrollVertically(1);
  }

  /**
   * Returns true iff the {@link RecyclerView} is scrolled to the
   * top of its content (i.e. its first item is completely visible).
   */
  private static boolean isRvScrolledToTop(RecyclerView rv) {
    final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
    return lm.findFirstVisibleItemPosition() == 0
        && lm.findViewByPosition(0).getTop() == 0;
  }
}
