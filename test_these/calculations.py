import pandas
from sklearn import preprocessing
import matplotlib.pyplot as plt

# read dataset
df = pandas.read_csv('Data.csv', usecols=[1,3,4,5,6,11,15], engine="python",delimiter=r";")
df.drop(df.index[:10], inplace=False)

# average dosing time calculation
dosing_duration = 0
nb_dosing = 0
average_time_dosing = 0

for key,row in df.iterrows():

	if row["MW101"] == 1:
		dosing_duration += 1
	elif dosing_duration > 0:
		average_time_dosing += dosing_duration
		nb_dosing = nb_dosing + 1
		dosing_duration = 0

if nb_dosing == 0:
	average_time_dosing = 0
else:
	average_time_dosing = average_time_dosing / nb_dosing

print("Average dosing time: "+str(average_time_dosing))

# average hole processus time
enc = preprocessing.LabelEncoder()

# show word's data distribution
for column in df:
	if len(set(df[column])) <= 1:
		print(column)
		df = df.drop('column_name', 1)
	enc.fit(df[column])
	ds = enc.transform(df[column])
	x = list(range(len(ds[:200])))

	fig = plt.figure(column)
	plt.plot(x, ds[:200], label='linear')
	fig.show()
	fig.savefig("figures/"+column+".png", dpi=100)


# show entire states distribution
dataset = [str(value["MW17"])+" "+str(value["MW89"])+" "+str(value["MW90"])+" "+str(value["MW91"])+" "+str(value["MW92"])+" "+str(value["MW97"])+" "+str(value["MW101"])+"\n" for key,value in df.iterrows()]
		
enc.fit(dataset)
ds = enc.transform(dataset)

x = list(range(200))
plt.plot(x, ds[:200], label='linear')
plt.show()


