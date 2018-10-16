import pandas
from sklearn import preprocessing
import matplotlib.pyplot as plt

# read csv dataset
df = pandas.read_csv('Data.csv', usecols=[1,3,4,5,6,11,15], engine="python",delimiter=r";")
df.drop(df.index[:10], inplace=False)
dataset =[]
rows = ""

# select pertinent features
for key,value in df.iterrows():
		row = str(value["MW17"])+" "+str(value["MW89"])+" "+str(value["MW90"])+" "+str(value["MW91"])+" "+str(value["MW92"])+" "+str(value["MW97"])+" "+str(value["MW101"])+"\n"
		rows +=row
		dataset.append(row)

# dataset file creator
datafile = open("outliers_detection/dataset.csv","w")
datafile.write(rows)
datafile.close

# Encode statements
enc = preprocessing.LabelEncoder()
enc.fit(dataset)
items = list(enc.transform(dataset))

# Create itemset file
"""
the items are used in the construction of association rules
patterns starts when previous sequence of dosaging end
automating is dosing when MW101 is on 1
one item involve sequences that precede dosing sequences and
dosing sequence
"""
rows = ""
row = ""
prec_state = 0

# feature engeenering : create sequential items
for idx in range(len(items)):
	if df["MW101"].loc[idx] >= prec_state:
		row += str(items[idx])+","
		prec_state += df["MW101"].loc[idx]
	else:
		prec_state = df["MW101"].loc[idx]
		rows += row+"\n"
		row = str(items[idx])+","
if len(row)>0 :
	rows += row+"\n"

# show pattern distribution
plt.scatter(list(range(len(items))), items, c ='red')
plt.show()

# save items on file
itemsfile = open("pattern_detection/Itemsets.csv","w")
itemsfile.write(rows)
itemsfile.close

# plot: show features distribution
x=pandas.value_counts(df['MW92'])   #  comptage  de  la  fréquence  la modalité MW92
y=pandas.value_counts(df['MW91'])   #  comptage  de  la  fréquence  la modalité MW91 
z=pandas.value_counts(df['MW97'])   #  comptage  de  la  fréquence  la modalité MW97
w=pandas.value_counts(df['MW101'])  #  comptage  de  la  fréquence  la modalité MW101

dico_x=dict(x) # Transformer x en un dictionnaire
dico_y=dict(y) # Transformer y en un dictionnaire
dico_z=dict(z) # Transformer z en un dictionnaire
dico_w=dict(w) # Transformer w en un dictionnaire

print("\ndico_x: ",dico_x,"\n")
print("dico_y: ",dico_y,"\n")

# Transformation du dictionnaire en dataframe
#  récupérer  les  clés  du  dictionnaires.  Ces  clés  représentent les modalités analysées. Elles seront les variables dans le dataframe à créer.
col_x= dico_x.keys()     
col_y= dico_y.keys() 
col_z= dico_z.keys()     
col_w= dico_w.keys() 

print("col_x : ",col_x,"\n")
print("col_y : ",col_y,"\n")

#  index=[0]  must be specified when the 
df_x  =  pandas.DataFrame(dico_x,columns=col_x,  index=[0]) 
df_y  =  pandas.DataFrame(dico_y,columns=col_y,  index=[0])  
df_z  =  pandas.DataFrame(dico_z,columns=col_z,  index=[0]) 
df_w  =  pandas.DataFrame(dico_w,columns=col_w,  index=[0])  


freq_values_x  =  []  # columns correspond to the values
for key in col_x:
	freq_values_x.append(df_x[key].max())

freq_values_y  =  [] 
for key in col_y:
	freq_values_y.append(df_y[key].max())

freq_values_z  =  [] 
for key in col_z:
	freq_values_z.append(df_z[key].max())

freq_values_w  =  [] 
for key in col_w:
	freq_values_w.append(df_w[key].max())


print(freq_values_x)
print(freq_values_y)
# Représentation graphique
# cadre du graphique

# -------------------------------------------------
import matplotlib.pyplot as plt
# -------------------------------------------------

# fig, ax = plt.subplots(figsize=(10,5)) 
fig  = plt.figure('myfigure',figsize=(15,8))  #  Nom  et  taille  de  la figure largeur/hauteur
# Cadran pre_score
ax = fig.add_subplot(221) 
x_pos = list(range(len(col_x))) 
# Création de l'axe des barres 
plt.bar(x_pos,freq_values_x, align='center',alpha=0.5 , color='r')  # Création du graphique 
plt.grid() # ajouter des grid (grilles) 
max_y = max(freq_values_x) #Graduation de l'axe y 
plt.ylim([0, max_y* 1.1]) 
plt.ylabel('frequencies') # labeliser les axes 
plt.xlabel('modalities') 
plt.title('Frequency diagram MW92') 
plt.xticks(x_pos, col_x) 


# # ---------------------- GRAPHE 2

ax = fig.add_subplot(222) 
y_pos = list(range(len(col_y))) 
# Création de l'axe des barres 
plt.bar(y_pos,freq_values_y, align='center',alpha=0.5, color='g')  # Création du graphique 
plt.grid() # ajouter des grid (grilles) 
max_y = max(freq_values_y) #Graduation de l'axe y 
plt.ylabel('frequencies') # labeliser les axes 
plt.xlabel('modalities') 
plt.title('Frequency diagram MW91') 
plt.xticks(y_pos, col_y) 



# # ---------------------- GRAPHE 3
ax = fig.add_subplot(223) 
z_pos = list(range(len(col_z))) 
# Création de l'axe des barres 
plt.bar(z_pos,freq_values_z, align='center',alpha=0.5 , color='b')  # Création du graphique 
plt.grid() # ajouter des grid (grilles) 
max_y = max(freq_values_z) #Graduation de l'axe y 
plt.ylabel('frequencies') # labeliser les axes 
plt.xlabel('modalities') 
plt.title('Frequency diagram MW97') 
plt.xticks(z_pos, col_z) 

# # ---------------------- GRAPHE 4 
ax = fig.add_subplot(224) 
w_pos = list(range(len(col_w))) 
# Création de l'axe des barres 
plt.bar(w_pos,freq_values_w, align='center',alpha=0.5)  # Création du graphique 
plt.grid() # ajouter des grid (grilles) 
max_y = max(freq_values_w) #Graduation de l'axe y 
plt.ylabel('frequencies') # labeliser les axes 
plt.xlabel('modalities') 
plt.title('Frequency diagram MW101') 
plt.xticks(w_pos, col_w) 

# # ----------------------

plt.show()  
plt.cla() 
plt.clf()  
plt.close()
# # ----------------------
# # ----------------------





	



