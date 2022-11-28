import mido
import math


def get_key(key, minor):
    keys = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']
    if minor:
        return keys[key] + 'm'
    return keys[key]


def detect_key(file):
    d = [0] * 12

    for track in file.tracks[1:]:
        for msg in track:
            if msg.type == 'note_on':
                print('on', msg.note, msg.time)
            if msg.type == 'note_off':
                print('off', msg.note, msg.time)
                d[msg.note % 12] += msg.time

    print(d)

    def correlation(a, b):
        mean_a = sum(a) / len(a)
        mean_b = sum(b) / len(b)
        numerator = 0
        denominator1 = 0
        denominator2 = 0
        for i in range(len(a)):
            numerator += (a[i] - mean_a) * (b[i] - mean_b)
            denominator1 += (a[i] - mean_a) ** 2
            denominator2 += (b[i] - mean_b) ** 2
        denominator = math.sqrt(denominator1 * denominator2)
        return numerator / denominator

    major_profile = [17.7661, 0.145624, 14.9265, 0.160186, 19.8049, 11.3587, 0.291248, 22.062, 0.145624, 8.15494,
                     0.232998,
                     4.95122]
    minor_profile = [18.2648, 0.737619, 14.0499, 16.8599, 0.702494, 14.4362, 0.702494, 18.6161, 4.56621, 1.93186,
                     7.37619,
                     1.75623]

    max_r = - (10 ** 6)
    key = 0
    minor = False

    correlations_maj = []
    correlations_min = []

    # Checking for major keys
    for i in range(12):
        correlations_maj.append(correlation(major_profile, d))
        d = d[1:] + d[0:1]

    for i in range(12):
        correlations_min.append(correlation(minor_profile, d))
        d = d[1:] + d[0:1]

    for i in range(12):
        if correlations_maj[i] > max_r:
            max_r = correlations_maj[i]
            key = i

    for i in range(12):
        if correlations_min[i] > max_r:
            max_r = correlations_min[i]
            key = i
            minor = True

    print(key, minor, max_r)
    print(get_key(key, minor))

    return key, minor


file = mido.MidiFile('input3.mid')
detect_key(file)
