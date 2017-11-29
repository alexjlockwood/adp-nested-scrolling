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
class CustomNestedScrollView2 extends NestedScrollView2 {

  public CustomNestedScrollView2(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void onNestedPreScroll(@NonNull View target, int dx, int dy, int[] consumed, int type) {
    final RecyclerView rv = (RecyclerView) target;
    final LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
    final boolean isRvScrolledToTop =
        lm.findFirstVisibleItemPosition() == 0 && lm.findViewByPosition(0).getTop() == 0;
    final boolean isNsvScrolledToBottom = !canScrollVertically(1);
    if ((dy < 0 && isRvScrolledToTop) || (dy > 0 && !isNsvScrolledToBottom)) {
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
}
