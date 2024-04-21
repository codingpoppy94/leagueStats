import json
import configparser
import os
import pandas as pd


conf = configparser.ConfigParser()
conf.read('./config.ini', encoding='utf-8')

file_list = os.listdir(conf['REPLAYPATH']['path'])

file_list_py = [file for file in file_list if file.endswith('.rofl')]
file_list_py.sort()


#
# vspm_weight = [1.206, 0.981, 0.957, 1.034, 1.409]
# dpm_weight = [558.534, 591.552, 463.059, 534.064, 650.872]
# dtpm_weight = [804.506, 1296.308, 866.541, 1093.081, 663.204]
# ccpm_weight = [1.531, 1.659, 2.724, 1.958, 2.57]
# kprate_weight = [2, 2, 4, 1.33, 2]
# dpg_weight = [12.103, 10.576, 12.273, 12.432, 10.402]
# piwa_weight = [0.2, 0.4, 0.3, 0.1, 0.7]

res_list = list()
for file_name in file_list_py:
    file = open(conf['REPLAYPATH']['path']+file_name, 'rb')
    file_byte = file.readlines()

    game_start_time = file_name.split('_')
    print(game_start_time)

    byte = None
    for byte in file_byte:
        if byte.find(b'{"gameLength":') > 0 and byte.find(b'"gameVersion":') > 0 and byte.find(b'"statsJson":') > 0:
            break

    start_idx = byte.find(b'{"gameLength":')
    end_idx = byte.rfind(b'\\"}]"}')

    byte = byte[start_idx:end_idx+6].replace(b'\\', b'').replace(b'"[', b'[').replace(b']"', b']')
    print(byte)
    data = byte.decode('utf-8')
    json_data = json.loads(data)

    print(json_data)
    # result_list_win = list()
    # result_list_lose = list()

    for d in json_data['statsJson']:
        try:
            res_list.append({'TEAM': game_start_time[0].upper(), 'MONTH': game_start_time[1][:2],
                                'DAY': game_start_time[1][2:4], 'HOUR': game_start_time[2][:2],
                                'MINUTE': game_start_time[2][2:4], 'IN_GAME_TEAM': d['TEAM'].replace('100', 'BLUE').replace('200','RED'),
                                'POSITION': d['TEAM_POSITION'].replace('BOTTOM', 'ADC').replace('MIDDLE', 'MID').replace('JUNGLE', 'JUG').replace('UTILITY', 'SUP'), 'NAME': conf['PLAYERSMAP'][d['NAME']], 'ASSISTS': d['ASSISTS'], 'CHAMPIONS_KILLED': d['CHAMPIONS_KILLED'], 'GOLD_EARNED': d['GOLD_EARNED'],
                                'NUM_DEATHS'
                             : d['NUM_DEATHS'], 'SKIN': conf['CHAMPIONSMAP'][d['SKIN']], 'TIME_CCING_OTHERS': d['TIME_CCING_OTHERS'],
                                'TIME_PLAYED': d['TIME_PLAYED'], 'TIME_PLAYED_MINUTE': int(int(d['TIME_PLAYED']) / 60), 'TIME_PLAYED_SECOND': int(d['TIME_PLAYED']) % 60, 'TOTAL_DAMAGE_DEALT_TO_CHAMPIONS': d['TOTAL_DAMAGE_DEALT_TO_CHAMPIONS'],
                                'TOTAL_DAMAGE_TAKEN': d['TOTAL_DAMAGE_TAKEN'], 'VISION_SCORE': d['VISION_SCORE'],
                                'VISION_WARDS_BOUGHT_IN_GAME': d['VISION_WARDS_BOUGHT_IN_GAME'], 'WIN': d['WIN'].replace('Win', '승').replace('Fail', '패')})
        except Exception:
            res_list.append({'TEAM': game_start_time[0].upper(), 'MONTH': game_start_time[1][:2],
                                'DAY': game_start_time[1][2:4], 'HOUR': game_start_time[2][:2],
                                'MINUTE': game_start_time[2][2:4], 'IN_GAME_TEAM': d['TEAM'].replace('100', 'BLUE').replace('200','RED'),
                'POSITION': d['TEAM_POSITION'].replace('BOTTOM', 'ADC').replace('MIDDLE', 'MID').replace('JUNGLE', 'JUG').replace('UTILITY', 'SUP'), 'NAME': d['NAME'], 'ASSISTS': d['ASSISTS'],
                                    'CHAMPIONS_KILLED': d['CHAMPIONS_KILLED'], 'GOLD_EARNED': d['GOLD_EARNED'],
                                    'NUM_DEATHS': d['NUM_DEATHS'], 'SKIN': conf['CHAMPIONSMAP'][d['SKIN']],
                                    'TIME_CCING_OTHERS': d['TIME_CCING_OTHERS'],
                                    'TIME_PLAYED': d['TIME_PLAYED'], 'TIME_PLAYED_MINUTE': int(int(d['TIME_PLAYED']) / 60), 'TIME_PLAYED_SECOND': int(d['TIME_PLAYED']) % 60,
                                    'TOTAL_DAMAGE_DEALT_TO_CHAMPIONS': d['TOTAL_DAMAGE_DEALT_TO_CHAMPIONS'],
                                    'TOTAL_DAMAGE_TAKEN': d['TOTAL_DAMAGE_TAKEN'],
                                    'VISION_SCORE': d['VISION_SCORE'],
                                    'VISION_WARDS_BOUGHT_IN_GAME': d['VISION_WARDS_BOUGHT_IN_GAME'],
                                    'WIN': d['WIN'].replace('Win', '승').replace('Fail', '패')})


df = pd.json_normalize(res_list)
df.to_csv('./summary_'+conf['REPLAYPATH']['path'].split('/')[-2]+'.csv', index=False, encoding='cp949')