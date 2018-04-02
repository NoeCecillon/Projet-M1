#********** model de belman ****************

using Gurobi
using JuMP
using CPUTime

File=string(ARGS[1])

#Initialisation

#Nb commodites
csvfilename=File*"fichier_3.csv"
csvdata = readcsv(csvfilename , header=true)
data = csvdata[1]
K=length(data)/3
K = Int(K)
#Nb arcs
csvfilename=File*"fichier_1.csv"
csvdata = readcsv(csvfilename , header=true)
data = csvdata[1]
v=length(data)/4
v = Int(v)
#Nb noeuds
n=findmax(data[:,2])[1]
n = Int(n)
#Read the instance from csv file 

csvfilename=File*"fichier_1.csv"
csvdata = readcsv(csvfilename , header=true)
data = csvdata[1]
header=csvdata[2]
p1=data[:,1]
p2=data[:,2]
p3=data[:,3]
p4=data[:,4]
v=Int(v)
T=zeros(n,n)
c=zeros(n,n)
f=zeros(n,n)
fi=zeros(K)
g=zeros(n,n,K)
o=zeros(K)
d=zeros(K)
for i=1:v
   # T is the topologie matrix 
   
   T[Int(p1[i]),Int(p2[i])]=1
   T[Int(p2[i]),Int(p1[i])]=1
   
   #f is the matrix of the fixed cost
   
   f[Int(p1[i]),Int(p2[i])]=p3[i]
   f[Int(p2[i]),Int(p1[i])]=p3[i]
   
   #c represents the lengths of the edges 
   
   c[Int(p1[i]),Int(p2[i])]=p4[i]
   c[Int(p2[i]),Int(p1[i])]=p4[i]
end
csvfilename=File*"fichier_3.csv"
csvdata = readcsv(csvfilename , header=true)
data = csvdata[1]
header=csvdata[2]
p5=data[:,1]
p6=data[:,2]
p7=data[:,3]
for i=1:K
  fi[i]=p5[i]  #The flow od each commodity
  o[i]=p6[i]   #Origin vector
  d[i]=p7[i]   #Destination vector
end
csvfilename=File*"fichier_2.csv"
csvdata = readcsv(csvfilename , header=true)
data = csvdata[1]
header=csvdata[2]
p8=data
#g is the vaiable costs matrix
for i=1:v,k=1:K
   g[Int(p1[i]),Int(p2[i]),k]=p8[i,k]
   g[Int(p2[i]),Int(p1[i]),k]=p8[i,k]
end


#Calcul the angle alpha


u=zeros(n,n)
w=zeros(v)
ww=zeros(v)
for i=1:n
for j=1:n
 if T[i,j]==1
    #u[i,j]=f[i,j]
	for k=1:K
	  u[i,j]=u[i,j]+fi[k]*g[i,j,k]
	end
end end end
k=1
for i=1:n
  for j=i+1:n
       if (T[i,j]==1)
         w[k]=u[i,j]
		 ww[k]=c[i,j]
		 k=k+1
		end
	end
end
#calcul de l'angle w.ww
  s=0
  for i=1:v
     s=s+w[i]*ww[i]
  end
  s=s/(norm(w)*norm(ww))
  if(s<1)
       alpha=acos(s)
   end

   
   
# initialisation de b
b=ones(n,K)
for i=1:n
 for j=1:K
   if i==o[j]
      b[i,j]=1
	elseif i==d[j]
	  b[i,j]=-1
	else
      b[i,j]=0
	end
 end
end


#calcul the big M


min=0
M=0
for i=1:n
  max=0
  for j=1:n
    if c[i,j]>max
     max=c[i,j]
    end


  #if c[i,j]>max
  #  max=c[i,j]
  #end
  if (c[i,j]<min)&&(T[i,j]==1)
    min=c[i,j]
  end
end
  M+=max
end
#M=n*max-min
M=M-min

# début du  model

   m_belman=Model(solver=GurobiSolver(TimeLimit=3600))


   #  ****déclaration des variables***


        @defVar(m_belman, x[1:n,1:n,1:K],Bin)
        @defVar(m_belman, y[1:n,1:n],Bin)
        @defVar(m_belman, pii[1:n,1:K]>=0)

   #  ****la fnction objective******

        @setObjective(m_belman,Min,(sum{fi[k]*g[i,j,k]*x[i,j,k],i=1:n,j=1:n,k=1:K}+sum{f[i,j]*y[i,j],i=1:n,j=i:n}))

   #  ****les contraintes***********


        for i=1:n,j=1:n @addConstraint(m_belman,y[i,j]==y[j,i]) end
        for i=1:n,k=1:K
            @addConstraint(m_belman,sum{x[i,j,k],j=1:n}-sum{x[j,i,k],j=1:n}==b[i,k])
     	end
        for i=1:n,j=1:n,k=1:K
            @addConstraint(m_belman,x[i,j,k]+x[j,i,k]<=y[i,j])
	        @addConstraint(m_belman,y[i,j]<=T[i,j])
	    if (i<j)
	          @addConstraint(m_belman,pii[i,k]-pii[j,k]<=M-y[i,j]*(M-c[i,j])-2*c[i,j]*x[j,i,k])
	    else
              @addConstraint(m_belman,pii[i,k]-pii[j,k]<=M-y[j,i]*(M-c[i,j])-2*c[i,j]*x[j,i,k])
	    end
    	end
	for i=1:n,k=1:K
           if (i==Int(d[k]))
               @addConstraint(m_belman,pii[Int(d[k]),k]==0)
           else
               @addConstraint(m_belman,pii[i,k]>=0)
           end
    end
    solve(m_belman)
    x=getvalue(x)
    y=getvalue(y)


#fin du model

#affichage du resultats

    #affichage du reseau y
        print("subgraph :")
        for i=1:n,j=1:i
             if y[i,j]==1   print("   " ,(i,j)) end
        end
        println(" ")

   #affichage des chemins et ses langueurs de chaque k
    lang=0
    for k=1:K
       long=0
       print("commodity ", k, " : " )
       for i=1:n,j=1:n
          long+=c[i,j]*x[i,j,k]
          if x[i,j,k]==1  print("   " ,(i,j)) end
       end
       println(" ")
       println("length : ", long)
       lang+=long
    end
#affichage du langueur total du reseau
println( " lang total est = ", lang)
Cout_belman=getobjectivevalue(m_belman)
Node_belman=getnodecount(m_belman::Model)
CPU_belman=getsolvetime(m_belman::Model)
best=getobjbound(m_belman::Model)
Gape_belman=abs(Cout_belman-best)/Cout_belman
solve(m_belman::Model;relaxation=true)
rlx_belman=getobjectivevalue(m_belman)
