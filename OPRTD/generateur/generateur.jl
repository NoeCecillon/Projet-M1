# ARGS[1] = nombre de noeuds
# ARGS[2] = densité
# ARGS[3] = angle alpha
# ARGS[4] = nombre de commodités

#formule pour calculer le nombre d'arcs : v= (N(N-1)*DD)/2

#density
DD=parse(Float64, ARGS[2])
#Number of nodes1
N=parse(Int64, ARGS[1])
# 0°<=alpha<=10°
eps= parse(Int64, ARGS[3])
for n in N
#number of commodities
KK= parse(Int64, ARGS[4])
for K in KK
K=Int(K)
for dd in DD
	
	v= round(Int64, (N*(N-1)*DD)/2)
  
for nexp=1:5
I1=ones(n)
I2=ones(n)
T=zeros(n,n)
f=zeros(n,n)
c=zeros(n,n)
u=zeros(n,n)
w=zeros(v)
g=zeros(n,n,K)
fi=zeros(K)
for i=1:K
   fi[i]=rand(1:5)
end
for i=1:n
   I1[i]=i
end
T=zeros(n,n)
for k=1:n
   s=0
   while(s==0)
      i=rand(1:n)
	  if (I1[i]>0)
	      I2[k]=I1[i]
		  I1[i]=0
		  s=1
	  end
	end
end
for i=1:(n-1)
   T[Int(I2[i]),Int(I2[i+1])]=1
   T[Int(I2[i+1]),Int(I2[i])]=1
end
T[Int(I2[n]),Int(I2[1])]=1
T[Int(I2[1]),Int(I2[n])]=1
for k=1:(v-n)
   i=rand(1:n)
   j=rand(1:n)
   while ((i==j)||(T[i,j]==1))
     j=rand(1:n)
   end
   T[i,j]=1
   T[j,i]=T[i,j]
end
for i=1:n    for j=i+1:n
  if (T[i,j]==1)
      f[i,j]=rand(1:50)
      f[j,i]=f[i,j]
      for k=1:K
         g[i,j,k]=rand(1:50)
         g[j,i,k]=g[i,j,k]
      end
  end
end    end
for i=1:n
for j=1:n
 #if T[i,j]==1
  #  u[i,j]=f[i,j]
	for k=1:K
	  u[i,j]=u[i,j]+fi[k]*g[i,j,k]
	#end
end end end
k=1
for i=1:n
  for j=i+1:n
       if (T[i,j]==1)
         w[k]=u[i,j]
		 k=k+1
		end
	end
end
o=zeros(K)
d=zeros(K)
for i=1:K
  o[i]=rand(1:n)
  d[i]=rand(1:n)
  while (d[i]==o[i])
     d[i]=rand(1:n)
  end
end
ww=zeros(v)
wmax=zeros(v)
alpha_max =1*(pi/18)
#alpha1=pi/19
  for i=1:v
    ww[i]=rand(1:50)
  end
  #calcul de l'angle w.ww
  s=0
  for i=1:v
     s=s+w[i]*ww[i]
  end
  s=s/(norm(w)*norm(ww))
  println("s=",s)
  if(s<1)
       alpha1=acos(s)
   end
println("alpha=",(alpha1*180)/pi)
wmax=ww
while (alpha1>alpha_max)
	bi = 2
	while (bi>=1)
	   landa=rand()
	   ww=landa*w+(1-landa)*wmax
	   s=0
       for i=1:v
          s=s+w[i]*ww[i]
        end
	   bi =s/(norm(w)*norm(ww))
	end
	alpha1=acos(bi)
end
k=1
for i=1:n
  for j=i+1:n
       if (T[i,j]==1)
         c[i,j]=ww[k]
		 c[j,i]=c[i,j]
		 k=k+1
		end
	end
end
G=zeros(v,K)
k1=1
for i=1:n , j=i+1:n
   if T[i,j]==1
      for k=1:K
	      G[k1,k]=g[i,j,k]
	  end
	  k1=k1+1
   end
end
resultfile = open("generateur/res/fichier_1.csv", "w")
println(resultfile, "start_node, end_node , cout_fixe , langueur")
for i=1:n,j=i+1:n
   if (T[i,j]==1)
	println(resultfile, "  $i   ,  $j   ,   $(f[i,j])   ,   $(c[i,j])  ")
   end
end
close(resultfile)
resultfile = open("generateur/res/fichier_2.csv", "w")
println(resultfile, " cout variable ")
for i=1:v
   for k=1:K
	print(resultfile, "  $(G[i,k]) , ")
   end
   println(resultfile," ")
end
close(resultfile)
resultfile = open("generateur/res/fichier_3.csv", "w")
println(resultfile,"nombre  , origines   ,  destinations")
for i=1:K
    println(resultfile, " $(fi[i])   ,   $(o[i])   ,   $(d[i]) ")
end
close(resultfile)

end
end
end
end
