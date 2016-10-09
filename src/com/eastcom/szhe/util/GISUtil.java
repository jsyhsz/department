package com.eastcom.szhe.util;

import java.util.ArrayList;
import java.util.List;

public class GISUtil {
	
	/**
	 * 判断指定点是否在某个区域内
	 * 该方法构思巧妙：
      1、向X轴正方向发射射线，先判断是否与多边形的线段相交，若相交点的X值大于P的x值，则计数器加1.
      2、通过大于等于线段两个端点的最小Y值，小于线段两个端点的最大Y值，判断出射线与线段是否相交。——避免了P在线段的延长线上的情况对计算结果的困扰。
	 * @param pnt1
	 * @param fencePnts
	 * @return
	 */
	public static boolean pointInFences(Point pnt1, Point[] fencePnts) {
		int j = 0, cnt = 0;
		for (int i = 0; i < fencePnts.length; i++) {
			j = (i == fencePnts.length - 1) ? 0 : j + 1;
			if ((fencePnts[i].getY() != fencePnts[j].getY())
					&& (((pnt1.getY() >= fencePnts[i].getY()) && (pnt1.getY() < fencePnts[j]
							.getY())) || ((pnt1.getY() >= fencePnts[j].getY()) && (pnt1
							.getY() < fencePnts[i].getY())))
					&& (pnt1.getX() < (fencePnts[j].getX() - fencePnts[i]
							.getX())
							* (pnt1.getY() - fencePnts[i].getY())
							/ (fencePnts[j].getY() - fencePnts[i].getY())
							+ fencePnts[i].getX()))
				cnt++;
		}
		return (cnt % 2 > 0) ? true : false;
	}
	
	/**
	 * 备注：有bug，如果要验证的点为边界点，可能判断错误。
	 * 判 断指定的经纬度坐标点是否落在指定的多边形区域内
	  @param ALon   指定点的经度
	  @param ALat   指定点的纬度
	  @param APoints   指定多边形区域各个节点坐标
	  @return True 落在范围内 False 不在范围内
		
	该题思想是向由点P向x正方向发射一个射线，穿过多边形线段上的个数为奇数则在多边形内，偶数则在多边形外
            具体方法是：点的Y值大于等于多边形上某个线段的最小值且小于该线段上的最大值，在该线段上取一个y值为点P.y的点P1。如果P.x<P1.x ，则计数器加1，若计数器为奇数则在多边形内，若为偶数则在多边形外
	 */
	 public static boolean isPtInPoly(double ALon, double ALat, List<Point> points)
     {
         int iSum = 0, iCount;
         double dLon1, dLon2, dLat1, dLat2, dLon;
         
         if (points.size() < 3)
             return false;
         
         iCount = points.size();
         for (int i = 0; i < iCount - 1; i++)
         {
             if (i == iCount - 1)
             {
                 dLon1 = points.get(i).getX();
                 dLat1 = points.get(i).getY();
                 dLon2 = points.get(0).getX();
                 dLat2 = points.get(0).getY();
             }
             else
             {
                 dLon1 = points.get(i).getX();
                 dLat1 = points.get(i).getY();
                 dLon2 = points.get(i + 1).getX();
                 dLat2 = points.get(i + 1).getY();
             }
             //以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
             if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1)))
             {
                 if (Math.abs(dLat1 - dLat2) > 0)
                 {
                     //得到 A点向左射线与边的交点的x坐标：
                     dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat)) / (dLat1 - dLat2);

                     // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                     if (dLon < ALon)
                         iSum++;
                 }
             }
         }
         if (iSum % 2 != 0)
             return true;
         return false;
     }
	
	public static void main(String[] args)
	{
		ArrayList<Point> points = new ArrayList<>();
		
		points.add(new Point(121.40180843382348,31.22298908627104));
		points.add(new Point(121.4062083826101,31.22438906997588));
		points.add(new Point(121.40900835001976,31.224489068811938));
		points.add(new Point(121.41070833023278,31.22548905717253));
		points.add(new Point(121.41370829531456,31.225289059500412));
		points.add(new Point(121.41440828716698,31.22618904902495));
		points.add(new Point(121.41630826505212,31.22638904669707));
		points.add(new Point(121.41800824526511,31.224989062992233));
		points.add(new Point(121.41707849059905,31.22058825285893));
		points.add(new Point(121.41807847895964,31.216988294760785));
		points.add(new Point(121.41847847430388,31.215788308728072));
		points.add(new Point(121.41307853715666,31.216988294760785));
		points.add(new Point(121.40857858953399,31.21668829825261));
		points.add(new Point(121.40527862794403,31.21658829941655));
		points.add(new Point(121.40297865471466,31.216888295924726));
		points.add(new Point(121.40247866053436,31.21838827846562));
		points.add(new Point(121.40257865937042,31.220388255186812));
		
		Point[] p = new Point[points.size()];
		points.toArray(p);
		
		Point dest1 = new Point(121.40180843382348,31.22298908627104);
		Point dest2 = new Point(121.40180843382348,31.20298908627104);
		
		System.out.println(GISUtil.pointInFences(dest1,p));
		System.out.println(GISUtil.pointInFences(dest2,p));	
		
		System.out.println(GISUtil.isPtInPoly(121.40180843382348,31.22298908627104,points));
		System.out.println(GISUtil.isPtInPoly(121.40180843382348,31.20298908627104,points));	}

}
