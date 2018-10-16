import pyfpgrowth
import pandas

# read csv dataset
df = pandas.read_csv('Itemsets.csv', engine="python",delimiter=r",")
df.drop(df.index[[11075,11076]])
dataset = df.values.tolist()

# train model
patterns = pyfpgrowth.find_frequent_patterns(dataset, 2)

# generate rules
rules = pyfpgrowth.generate_association_rules(patterns, 0.7)

print(rules)
