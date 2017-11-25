package com.alexjlockwood.example.designlib;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import static android.support.design.widget.CoordinatorLayout.Behavior;

/**
 * A custom {@link Behavior} used to block touch events that do not originate on
 * top of the {@link MainActivity}'s card view or FAB. It also is used to
 * adjust the layout so that the UI is displayed properly.
 */
class CustomBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {

  public CustomBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean layoutDependsOn(
      CoordinatorLayout parent, NestedScrollView child, View dependency) {
    // List the toolbar container as a dependency to ensure that it will
    // always be laid out before the child (which depends on the toolbar
    // container's height in onLayoutChild() below).
    return dependency.getId() == R.id.toolbar_container;
  }

  @Override
  public boolean onLayoutChild(
      CoordinatorLayout parent, NestedScrollView child, int layoutDirection) {
    // First layout the child as normal.
    parent.onLayoutChild(child, layoutDirection);

    // Center the FAB vertically along the top edge of the card.
    final int fabHalfHeight = child.findViewById(R.id.fab).getHeight() / 2;
    setTopMargin(child.findViewById(R.id.cardview), fabHalfHeight);

    // Give the RecyclerView a maximum height to ensure the card will never
    // overlap the toolbar as it scrolls.
    final int recyclerViewMaxHeight = child.getHeight() - fabHalfHeight
        - child.findViewById(R.id.card_title).getHeight()
        - child.findViewById(R.id.card_subtitle).getHeight();
    ((MaxHeightRecyclerView) child.findViewById(R.id.card_recyclerview))
        .setMaxHeight(recyclerViewMaxHeight);

    // Give the card container top padding so that only the top edge of the card
    // initially appears at the bottom of the screen. The total padding will
    // be the distance from the top of the screen to the FAB's top edge.
    final int toolbarContainerHeight = parent.getDependencies(child).get(0).getHeight();
    setPaddingTop(child.findViewById(R.id.card_container),
        recyclerViewMaxHeight - toolbarContainerHeight);

    // Offset the child's height so that its bounds don't overlap the toolbar container.
    ViewCompat.offsetTopAndBottom(child, toolbarContainerHeight);

    // Return true so that the parent doesn't waste time laying out the
    // child again (any modifications made above will have triggered a second
    // layout pass anyway).
    return true;
  }

  private static void setTopMargin(View view, int topMargin) {
    final MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
    if (lp.topMargin != topMargin) {
      lp.topMargin = topMargin;
      view.setLayoutParams(lp);
    }
  }

  private static void setPaddingTop(View view, int paddingTop) {
    if (view.getPaddingTop() != paddingTop) {
      view.setPadding(0, paddingTop, 0, 0);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(
      CoordinatorLayout parent, NestedScrollView child, MotionEvent ev) {
    // Block all touch events that originate within the bounds of our
    // NestedScrollView but do *not* originate within the bounds of its
    // inner CardView and FloatingActionButton.
    return ev.getActionMasked() == MotionEvent.ACTION_DOWN
        && isTouchInChildBounds(parent, child, ev)
        && !isTouchInChildBounds(parent, child.findViewById(R.id.cardview), ev)
        && !isTouchInChildBounds(parent, child.findViewById(R.id.fab), ev);
  }

  private static boolean isTouchInChildBounds(
      ViewGroup parent, View child, MotionEvent ev) {
    return ViewGroupUtils.isPointInChildBounds(
        parent, child, (int) ev.getX(), (int) ev.getY());
  }
}
