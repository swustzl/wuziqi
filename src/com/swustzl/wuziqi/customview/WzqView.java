package com.swustzl.wuziqi.customview;

import java.util.ArrayList;
import java.util.List;

import com.swustzl.wuziqi.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class WzqView extends View {

    /*
     * public WzqView(Context context) { super(context); // TODO Auto-generated constructor stub }
     */
    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<Point>();
    private List<Point> mBlackArray = new ArrayList<Point>();
    private Point mNowPoint = null;

    private boolean mIsGameOver = false;
    private boolean mIsWhiteWin;
    private static int MAX_PIECE = 5;

    public WzqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();

    }

    /*
     * public WzqView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); // TODO
     * Auto-generated constructor stub }
     */

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);

        mIsGameOver = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGramOver();
    }

    private void checkGramOver() {
        if (mIsWhite) {
            mIsGameOver = checkWhoWin(mBlackArray);
        } else {
            mIsGameOver = checkWhoWin(mWhiteArray);
        }
        if (mIsGameOver) {
            mIsWhiteWin = mIsWhite ? false : true;
            Toast.makeText(getContext(), mIsWhiteWin ? "白棋赢" : "黑棋赢", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkWhoWin(List<Point> mPieceArray) {
        boolean isEnd;
        if (mNowPoint != null) {
            isEnd = checkHorizontal(mPieceArray);
            if (isEnd)
                return true;
            isEnd = checkVertical(mPieceArray);
            if (isEnd)
                return true;
            isEnd = checkLeftSlant(mPieceArray);
            if (isEnd)
                return true;
            isEnd = checkRightSlant(mPieceArray);
            if (isEnd)
                return true;
        }
        return false;
    }

    private boolean checkRightSlant(List<Point> mPieceArray) {
        int count = 1;
        boolean before, after;
        before = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y));
        after = before;
        for (int i = 1; i < MAX_PIECE; i++) {
            if (!before && !after) {
                break;
            }
            if (before) {
                before = mPieceArray.contains(new Point(mNowPoint.x - i, mNowPoint.y + i));
                if (before)
                    count++;
            }
            if (after) {
                after = mPieceArray.contains(new Point(mNowPoint.x + i, mNowPoint.y - i));
                if (after)
                    count++;
            }
        }
        if (count >= MAX_PIECE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLeftSlant(List<Point> mPieceArray) {
        int count = 1;
        boolean before, after;
        before = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y));
        after = before;
        for (int i = 1; i < MAX_PIECE; i++) {
            if (!before && !after) {
                break;
            }
            if (before) {
                before = mPieceArray.contains(new Point(mNowPoint.x - i, mNowPoint.y - i));
                if (before)
                    count++;
            }
            if (after) {
                after = mPieceArray.contains(new Point(mNowPoint.x + i, mNowPoint.y + i));
                if (after)
                    count++;
            }
        }
        if (count >= MAX_PIECE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkVertical(List<Point> mPieceArray) {
        int count = 1;
        boolean before, after;
        before = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y));
        after = before;
        for (int i = 1; i < MAX_PIECE; i++) {
            if (!before && !after) {
                break;
            }
            if (before) {
                before = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y - i));
                if (before)
                    count++;
            }
            if (after) {
                after = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y + i));
                if (after)
                    count++;
            }
        }
        if (count >= MAX_PIECE) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkHorizontal(List<Point> mPieceArray) {
        int count = 1;
        boolean before, after;
        before = mPieceArray.contains(new Point(mNowPoint.x, mNowPoint.y));
        after = before;
        for (int i = 1; i < MAX_PIECE; i++) {
            if (!before && !after) {
                break;
            }
            if (before) {
                before = mPieceArray.contains(new Point(mNowPoint.x - i, mNowPoint.y));
                if (before)
                    count++;
            }
            if (after) {
                after = mPieceArray.contains(new Point(mNowPoint.x + i, mNowPoint.y));
                if (after)
                    count++;
            }
        }
        if (count >= MAX_PIECE) {
            return true;
        } else {
            return false;
        }
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            if (mIsGameOver) {
                return false;
            }
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            mNowPoint = p;
            if (mIsWhite) {
                mWhiteArray.add(mNowPoint);
            } else {
                mBlackArray.add(mNowPoint);
            }
            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
}
