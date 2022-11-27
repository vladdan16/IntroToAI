import mido
import math


def get_key(key, minor):
    keys = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']
    return keys[key] + minor


file = mido.MidiFile('input1.mid')

# for msg in file:
#     if (msg.type == 'note_on'):
#         print('on', msg.note, msg.time)
#     if (msg.type == 'note_off'):
#         print('off', msg.note, msg.time)

d = dict()

for i in range(12):
    d[i] = 0

for track in file.tracks[1:]:
    for msg in track:
        if msg.type == 'note_on':
            print('on', msg.note, msg.time)
        if msg.type == 'note_off':
            print('off', msg.note, msg.time)
            d[msg.note % 12] += msg.time

print(d)

mean_d = 0
for i in range(12):
    mean_d += d[i]
mean_d /= 12

major_profile = [6.35, 2.23, 3.48, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88]
mean_major = sum(major_profile) / len(major_profile)
minor_profile = [6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17]
mean_minor = sum(minor_profile) / len(minor_profile)
max_r = - (10 ** 6)
key = 0
minor = ''

# Checking for major keys
for i in range(12):
    s1 = 0
    s21 = 0
    s22 = 0
    for j in range(12):
        s1 += (major_profile[j] - mean_major) * (d[(j + 12 - i) % 12] - mean_d)
        s21 += (major_profile[j] - mean_major) ** 2
        s22 += (d[(j + 12 - i) % 12] - mean_d) ** 2
    s2 = math.sqrt(s21 * s22)
    r = s1 / s2
    if r > max_r:
        max_r = r
        key = i

for i in range(12):
    s1 = 0
    s21 = 0
    s22 = 0
    for j in range(12):
        s1 += (minor_profile[j] - mean_minor) * (d[(j + 12 - i) % 12] - mean_d)
        s21 += (minor_profile[j] - mean_minor) ** 2
        s22 += (d[(j + 12 - i) % 12] - mean_d) ** 2
    s2 = math.sqrt(s21 * s22)
    r = s1 / s2
    if r > max_r:
        max_r = r
        key = i
        minor = 'm'

print(key, minor, max_r)

print(get_key(key, minor))
