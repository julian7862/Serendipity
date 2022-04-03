import numpy as np
import pandas as pd
import torch
import re
import csv
import os
import sys
from fast_bert.learner_cls import BertLearner
from fast_bert.metrics import accuracy
from fast_bert.data_cls import BertDataBunch
import logging

df1 = pd.read_csv(r'C:/Users/mypc\Downloads/comments0907.csv', encoding='utf-8',header=None)
#抽取評論和label
df=df1[[3,5,6,7,8]]
df=df.rename(columns={3:'sentence',5:'label1',6:'label2',7:'label3',8:'label4'})
global mydict
def filter_str(desstr,restr=''):
    #过滤除中英文及数字以外的其他字符
    res = re.compile("[^\u4e00-\u9fa5^a-z^A-Z^0-9]")
    return res.sub(restr, desstr)

for i in range(0, len(df)):
    df.loc[i,'sentence']=filter_str(str(df.loc[i,'sentence']))
x=[]
for i in range(0, len(df)):
  for j in range(1,5):
   x.append(str(df.iloc[i,j]))
pd2=pd.DataFrame()

for i in range(len(df)):
    a=df.loc[i]
    d=pd.DataFrame(a).T
    pd2=pd2.append([d]*4)  #每行复制4倍

pd2=pd2.drop(columns=['label1', 'label2','label3','label4'])    
pd2['label']=x
pd3 = pd.concat([pd2, pd.get_dummies(pd2.label)], 1).groupby(['sentence']).sum().reset_index()
pd3=pd3.drop(columns=['nan']) 
df1=pd3.loc[1:1803]
df2=pd3.loc[1804:3606]
df1.to_csv(r'C:/Users/mypc\Downloads/train1.csv')
df2.to_csv(r'C:/Users/mypc\Downloads/valid1.csv') 

label_cols = ["下午茶",	"中式料理",	"冰品飲料",	"午餐",	"合菜",	"咖哩",	"咖啡",	"宵夜",	"寵物友善"	,"小吃"	,"居酒屋","拉麵",	"日本料理",	
              "早午餐",	"早餐","晚餐"	,"景觀餐廳",	"泰式料理"	,"港式料理",	"火鍋",	"燒烤",	"牛排",	"牛肉麵",	"甜點",	"精緻高級"	,"約會餐廳"	
              ,"素食"	,"美式料理","義式料理","蛋糕"	,"親子餐廳",	"韓式料理",	"餐酒館/酒吧"]


DATA_PATH = r'C:/Users/mypc\Downloads'     
LABEL_PATH = r'C:/Users/mypc\Downloads'


logger = logging.getLogger()
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(name)s - %(message)s',
    datefmt='%m/%d/%Y %H:%M:%S',
    handlers=[
        logging.StreamHandler(sys.stdout)
    ]
)

device_cuda = torch.device("cpu")
metrics = [{'name': 'accuracy', 'function': accuracy}]


if torch.cuda.device_count() > 1:
    multi_gpu = True
else:
    multi_gpu = False



databunch = BertDataBunch(DATA_PATH, LABEL_PATH, 
                          tokenizer='bert-base-chinese', 
                          train_file='train1.csv', 
                          val_file='valid1.csv',
                          label_file='label1.csv',
                          text_col="sentence",
                          label_col=label_cols,
                          batch_size_per_gpu=4,
                          max_seq_length=512,
                          multi_gpu=multi_gpu,
                          multi_label=True,
                          model_type='bert')

learner = BertLearner.from_pretrained_model(
						databunch,
						pretrained_path='bert-base-chinese',
						metrics=metrics,
						device=device_cuda,
						logger=logger,
					    output_dir=r'C:/Users/mypc\Downloads',
                        finetuned_wgts_path=None,
  					    warmup_steps=500,
						multi_gpu=multi_gpu,
						is_fp16=True,
						multi_label=True)

learner.fit(epochs=1, lr=12e-4, validate=True, schedule_type="warmup_linear",optimizer_type="lamb")

torch.save(learner,r'C:/Users/mypc\Downloads/new')
