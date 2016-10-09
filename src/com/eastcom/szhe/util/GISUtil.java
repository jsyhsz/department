package com.eastcom.szhe.util;

import java.util.ArrayList;
import java.util.List;

public class GISUtil {
	
	/**
	 * �ж�ָ�����Ƿ���ĳ��������
	 * �÷�����˼���
      1����X�������������ߣ����ж��Ƿ������ε��߶��ཻ�����ཻ���Xֵ����P��xֵ�����������1.
      2��ͨ�����ڵ����߶������˵����СYֵ��С���߶������˵�����Yֵ���жϳ��������߶��Ƿ��ཻ������������P���߶ε��ӳ����ϵ�����Լ����������š�
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
	 * ��ע����bug�����Ҫ��֤�ĵ�Ϊ�߽�㣬�����жϴ���
	 * �� ��ָ���ľ�γ��������Ƿ�����ָ���Ķ����������
	  @param ALon   ָ����ľ���
	  @param ALat   ָ�����γ��
	  @param APoints   ָ���������������ڵ�����
	  @return True ���ڷ�Χ�� False ���ڷ�Χ��
		
	����˼�������ɵ�P��x��������һ�����ߣ�����������߶��ϵĸ���Ϊ�������ڶ�����ڣ�ż�����ڶ������
            ���巽���ǣ����Yֵ���ڵ��ڶ������ĳ���߶ε���Сֵ��С�ڸ��߶��ϵ����ֵ���ڸ��߶���ȡһ��yֵΪ��P.y�ĵ�P1�����P.x<P1.x �����������1����������Ϊ�������ڶ�����ڣ���Ϊż�����ڶ������
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
             //��������ж�A���Ƿ��ڱߵ����˵��ˮƽƽ����֮�䣬��������н��㣬��ʼ�жϽ����Ƿ�����������
             if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1)))
             {
                 if (Math.abs(dLat1 - dLat2) > 0)
                 {
                     //�õ� A������������ߵĽ����x���꣺
                     dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat)) / (dLat1 - dLat2);

                     // ���������A����ࣨ˵������������ �ߵĽ��㣩����������ߵ�ȫ����������һ��
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
