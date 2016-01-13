package ch.fhnw.comgr.robosim;

import ch.fhnw.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.util.math.Mat4;




public class RobotSolver {
double alpha[];
double theta[]={0,0,0,0,0,0,0,0};
double theta_init[]={0 * (Math.PI / 180),0 * (Math.PI / 180), 90 * (Math.PI / 180),
		90* (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),90 * (Math.PI / 180),0,0};
double beta[]={0,0 * (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),0,0,0,0};
double[] d;
double[] a;
double l1=0.384;
double l2=0.386;
double l3=0.53;
double b[]={0,0,0,-0.0,0,0,0,0};
public RobotSolver(double[] d,double[] a,double[] alpha){
	this.alpha=alpha;
	this.d=d;
	this.a=a;
}
public RobotSolver(){
	
}

public List<Mat4> solve(double[] theta){
	for(int i=1;i<this.theta.length;i++){
		this.theta[i]=(theta[i])*(Math.PI/180)+theta_init[i];
	}
	return makeMatrix();
}
public double[] solveAngles(Vec3 pos){
	double[] theta=new double[8];
	if(pos.x<0){
		theta[1]=Math.toDegrees(Math.atan(pos.y/pos.x))-180;
	}
	else{
		theta[1]=Math.toDegrees(Math.atan(pos.y/pos.x));
	}
	
	double hypotenus=Math.sqrt(Math.pow(pos.x, 2)+Math.pow(pos.y,2));
	double h_temp=0;
	double z_temp=0;
	double alpha=0;
	double beta=0;
	double h1=0;
	double h2=0;
	double tol=0.014;
	if(hypotenus==0){
		theta[2]=90;
		theta[3]=0;
	}
	else{
	if((l1+l2)<pos.z){
		
		double l_temp=l2+l3;
		beta=Math.atan(hypotenus/l_temp);
		if(l1>pos.x){
			beta=Math.toRadians(90);
		}
		for(int i=0;i<15;i++){
			
			if(((h_temp-hypotenus) < tol && (h_temp-hypotenus) > -tol)){
				break;
			}
			double l_t1=l2*Math.sin(beta);
			double l_t2=pos.z-l_t1;
			
			double tmp_l=l_t2/l3;
			while(tmp_l>1){
				tmp_l=-1;
			}
			while(tmp_l<-1){
				tmp_l+=1;
			}
			alpha=Math.asin(tmp_l);
			double t_t1=l3*Math.sin(alpha);
			double t_t2=hypotenus-t_t1;
			tmp_l=t_t2/l2;
			while(tmp_l>1){
				tmp_l+=-1;
			}
			while(tmp_l<-1){
				tmp_l+=1;
			}
			beta=Math.asin(tmp_l);
			if(Math.sin(alpha+beta)*l3==h1||Math.sin(beta)*l2==h2){
				break;
			}
			h1=Math.sin(alpha+beta)*l3;
			h2=Math.sin(beta)*l2;
			double z1=Math.cos(alpha+beta)*l3;
			double z2=Math.cos(beta)*l2;
			
			h_temp=h1+h2;
			
			
		}
	}
	else{
		double l_temp=pos.z-l1-l2;
		alpha=Math.atan(hypotenus/l_temp);
		
		for(int i=0;i<15;i++){
			
		if(((h_temp-hypotenus) < tol && (h_temp-hypotenus) > -tol)){
			break;
		}
			double t_t1=l3*Math.sin(alpha);
			double t_t2=hypotenus-t_t1;
			double tmp_l=t_t2/l2;
			while(tmp_l>1){
				tmp_l-=1;
			}
			while(tmp_l<-1){
				tmp_l+=-1;
			}
			beta=Math.asin(tmp_l);
			double l_t1=l2*Math.sin(beta);
			double l_t2=pos.z-l_t1;
			tmp_l=l_t2/l3;
			while(tmp_l>1){
				tmp_l-=1;
			}
			while(tmp_l<-1){
				tmp_l+=-1;
			}
			alpha=Math.asin(tmp_l);
			if(Math.sin(alpha+beta)*l3==h1||Math.sin(beta)*l2==h2){
				break;
			}
			h1=Math.sin(alpha+beta)*l3;
			h2=Math.sin(beta)*l2;
			double z1=Math.cos(alpha)*l3;
			double z2=Math.cos(beta)*l2;
			
				h_temp=h1+h2;
				z_temp=z1+z2+l1;
			
		}
		
	}
	theta[2]=90-Math.toDegrees(beta);
	theta[3]=-Math.toDegrees(alpha);
	if(theta[3]>0&&theta[3]<90){
//		theta[3]=90-Math.toDegrees(alpha);
	}
	
	}
//	System.out.println("x: "+pos.x+" y: "+pos.y+" z: "+pos.z);
	System.out.println("x: "+pos.x+" y: "+pos.y+" z: "+pos.z+" alpha: "+theta[3]+" beta: "+theta[2]+" theta: "+theta[1]);
	
	return theta;
}
public double[] solveThetas(Vec3 pos, double t){
	double[] theta=new double[8];
	if(pos.x<0){
		theta[1]=Math.toDegrees(Math.atan(pos.y/pos.x))-180;
	}
	else{
		theta[1]=Math.toDegrees(Math.atan(pos.y/pos.x));
	}
	
	double t_x=Math.sqrt(Math.pow(pos.x, 2)+Math.pow(pos.y,2));
	double t_z=pos.z-l1;
	double temp1=(Math.pow(t_x, 2)+Math.pow(t_z, 2)-Math.pow(l2, 2)-Math.pow(l3, 2))/(2*l2*l3);
	double theta2_pos=Math.atan2(Math.sqrt(1-temp1), temp1);
	double theta2_neg=Math.atan2(-Math.sqrt(1-temp1), temp1);
	double temp2_pos=Math.cos(theta2_pos)*l3+l2;
	double theta1=Math.atan2(t_z, t_x)+Math.atan2(Math.sqrt(Math.pow(t_z, 2)+Math.pow(t_x, 2)-Math.pow(temp2_pos, 2)), temp2_pos);
	

	theta[2]=90-Math.toDegrees(theta1);
	theta[3]=-Math.toDegrees(theta2_neg);
	theta[5]=Math.toDegrees(t)-theta[2]-theta[3]-55;
	return theta;
}
public List<Vec3> solveLine(Vec3 p1,Vec3 p2){
	List<Vec3> Bahn=new ArrayList<>();
	double x=p2.x-p1.x;
	double y=p2.y-p1.y;
	double z=p2.z-p1.z;
	for(int i=0;i<100;i++){
		Bahn.add(i, new Vec3(p1.x+(x/100*i),p1.y+(y/100*i),p1.z+(z/100*i)));
	}
	return Bahn;
}
public List<double[]> makeLine(Vec3 p1,Vec3 p2,double theta){
	
	List<Vec3>	Bahn = this.solveLine(p1, p2);
	List<double[]>	angles=new ArrayList<>();
	for(int i=0;i<Bahn.size();i++){
		angles.add(i,this.solveThetas(Bahn.get(i),theta));
		
	}
	return angles;
}

public List<Mat4> makeMatrix(){
	List<Mat4> T=new ArrayList<>();
	Mat4 normal=new Mat4();
	for(int i=0;i<theta.length;i++){
		
		
		Mat4 first=new Mat4((float)Math.cos(theta[i]), (float)-Math.sin(theta[i]), 0, 0,
							(float)Math.sin(theta[i]), (float)Math.cos(theta[i]), 0, 0,
							0, 0, 1, 0,
							0, 0, 0, 1);

		Mat4 second=new Mat4((float)Math.cos(alpha[i]), 0,(float)Math.sin(alpha[i]), 0,
							0, 1,0, 0,
							(float)-Math.sin(alpha[i]), 0,(float)Math.cos(alpha[i]), 0,
				  			0, 0, 0, 1);
		Mat4 third=new Mat4(1,0,0,0,
							0, 1,0, 0,
							0, 0,(float)Math.cos(beta[i]), (float)-Math.sin(beta[i]),
							0, 0,(float)Math.sin(beta[i]), (float)Math.cos(beta[i])) ;
		if(i==0){
			normal=Mat4.multiply(first,second,third,Mat4.translate(0, 0, (float)d[i]),Mat4.translate((float)a[i], 0, 0));
		}
		else{
			normal=Mat4.multiply(normal,first,second,third,Mat4.translate(0, 0, (float)d[i]),Mat4.translate((float)a[i], 0, 0),Mat4.translate(0,(float)b[i], 0));
		}
		
		T.add(i,normal);
		}
	return T;
}
}
