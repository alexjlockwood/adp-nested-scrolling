package alexjlockwood.nestedscrolling;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * A {@link RecyclerView} with an optional maximum height.
 */
public class MaxHeightRecyclerView extends RecyclerView {
  private int mMaxHeight = -1;

  public MaxHeightRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    final int mode = MeasureSpec.getMode(heightSpec);
    final int height = MeasureSpec.getSize(heightSpec);
    if (mMaxHeight >= 0 && (mode == MeasureSpec.UNSPECIFIED || height > mMaxHeight)) {
      heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
    }
    super.onMeasure(widthSpec, heightSpec);
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