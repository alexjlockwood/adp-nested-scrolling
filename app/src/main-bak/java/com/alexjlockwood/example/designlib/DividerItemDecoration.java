package com.alexjlockwood.example.designlib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * A {@link RecyclerView.ItemDecoration} that can be used to draw dividers
 * between vertical list items.
 *
 * Override {@link #shouldDrawDivider(int)} to customize which list items should
 * and shouldn't have dividers drawn above them.
 */
class DividerItemDecoration extends RecyclerView.ItemDecoration {

  private final Drawable mDividerDrawable;
  private final int mDividerHeight;

  public DividerItemDecoration(Context ctx) {
    mDividerDrawable = new ColorDrawable(ContextCompat.getColor(ctx, R.color.divider));
    mDividerHeight = ctx.getResources().getDimensionPixelSize(R.dimen.divider_height);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
    super.getItemOffsets(outRect, view, parent, state);
    if (shouldDrawDividerAboveItem(parent.getChildAdapterPosition(view))) {
      outRect.top = mDividerHeight;
    }
  }

  @Override
  public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();

    for (int i = 0, size = parent.getChildCount(); i < size; i++) {
      final View child = parent.getChildAt(i);
      final int position = parent.getChildAdapterPosition(child);
      if (shouldDrawDividerAboveItem(position)) {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getTop() - lp.topMargin;
        final int bottom = top + mDividerHeight;
        mDividerDrawable.setBounds(left, top, right, bottom);
        mDividerDrawable.draw(canvas);
      }
    }
  }

  /**
   * Returns true iff a divider should be drawn above the view at the specified
   * position. The default implementation returns true for all positions.
   */
  protected boolean shouldDrawDividerAboveItem(int position) {
    return true;
  }
}