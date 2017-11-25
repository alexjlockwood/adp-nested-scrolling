package com.alexjlockwood.example.designlib;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * A simple utility class copied from the design support library source code
 * (see https://goo.gl/VJx5ks).
 */
final class ViewGroupUtils {
  private static final Matrix sMatrix = new Matrix();
  private static final RectF sRectF = new RectF();
  private static final Matrix IDENTITY = new Matrix();
  private static final Rect sRect = new Rect();

  /**
   * Check if a given point in the parent's coordinates are within
   * the view bounds of the given direct child view.
   *
   * @param child child view to test
   * @param x X coordinate to test, in the parent's coordinate system
   * @param y Y coordinate to test, in the parent's coordinate system
   * @return true if the point is within the child's bounds, false otherwise
   */
  public static boolean isPointInChildBounds(
      ViewGroup parent, View child, int x, int y) {
    getDescendantRect(parent, child, sRect);
    return sRect.contains(x, y);
  }

  /**
   * Retrieve the transformed bounding rect of an arbitrary descendant view.
   * This does not need to be a direct child.
   *
   * @param descendant descendant view to reference
   * @param out rect to set to the bounds of the descendant view
   */
  private static void getDescendantRect(
      ViewGroup parent, View descendant, Rect out) {
    out.set(0, 0, descendant.getWidth(), descendant.getHeight());
    offsetDescendantRect(parent, descendant, out);
  }

  /**
   * This is a port of the common
   * {@link ViewGroup#offsetDescendantRectToMyCoords(View, Rect)} from the
   * framework, but adapted to take transformations into account. The result
   * will be the bounding rect of the real transformed rect.
   *
   * @param descendant view defining the original coordinate system of rect
   * @param rect the rect to offset from descendant to this view's coordinate system
   */
  private static void offsetDescendantRect(
      ViewGroup parent, View descendant, Rect rect) {
    sMatrix.set(IDENTITY);
    offsetDescendantMatrix(parent, descendant, sMatrix);
    sRectF.set(rect);
    sMatrix.mapRect(sRectF);
    rect.set((int) (sRectF.left + 0.5f), (int) (sRectF.top + 0.5f),
        (int) (sRectF.right + 0.5f), (int) (sRectF.bottom + 0.5f));
  }

  private static void offsetDescendantMatrix(
      ViewParent target, View view, Matrix m) {
    final ViewParent parent = view.getParent();
    if (parent instanceof View && parent != target) {
      final View vp = (View) parent;
      offsetDescendantMatrix(target, vp, m);
      m.preTranslate(-vp.getScrollX(), -vp.getScrollY());
    }
    m.preTranslate(view.getLeft(), view.getTop());
    if (!view.getMatrix().isIdentity()) {
      m.preConcat(view.getMatrix());
    }
  }

  private ViewGroupUtils() {}
}