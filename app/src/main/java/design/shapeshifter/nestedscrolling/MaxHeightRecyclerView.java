package design.shapeshifter.nestedscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * A {@link RecyclerView} with an optional maximum height.
 */
class MaxHeightRecyclerView extends RecyclerView {
  private int mMaxHeight = -1;

  public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    final int mode = MeasureSpec.getMode(heightMeasureSpec);
    final int height = MeasureSpec.getSize(heightMeasureSpec);
    if (mMaxHeight >= 0 && (mode == MeasureSpec.UNSPECIFIED || height > mMaxHeight)) {
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  /**
   * Sets the maximum height for this recycler view.
   */
  public void setMaxHeight(int maxHeight) {
    if (mMaxHeight != maxHeight) {
      mMaxHeight = maxHeight;
      requestLayout();
    }
  }
}