import random
import mido
import math
import numpy
from enum import Enum
from typing import List, Tuple


class Note:
    def __init__(self, n):
        self.note = n % 12
        self.octave = n // 12

    def get_midi_number(self):
        return 12 * self.octave + self.note

    def get_note(self):
        return self.note

    def get_octave(self):
        return self.octave


class ChordType(Enum):
    major_triad = 0
    minor_triad = 1
    first_inverted_major = 2
    first_inverted_minor = 3
    second_inverted_major = 4
    second_inverted_minor = 5
    dim = 6
    sus2 = 7
    sus4 = 8


class Chord:
    def __init__(self, note, type):
        offset = []
        if type == ChordType.major_triad:
            offset = [0, 4, 7]
        elif type == ChordType.minor_triad:
            offset = [0, 3, 7]
        elif type == ChordType.first_inverted_major:
            offset = [12, 4, 7]
        elif type == ChordType.first_inverted_minor:
            offset = [12, 3, 7]
        elif type == ChordType.second_inverted_major:
            offset = [12, 16, 7]
        elif type == ChordType.second_inverted_minor:
            offset = [12, 15, 7]
        elif type == ChordType.dim:
            offset = [0, 3, 6]
        elif type == ChordType.sus2:
            offset = [0, 2, 7]
        elif type == ChordType.sus4:
            offset = [0, 5, 7]
        self.notes = []
        for i in range(3):
            n = Note(note.get_midi_number() + offset[i])
            self.notes.append(n)

    def get_midi_array(self):
        array = []
        for note in self.notes:
            array.append(note.get_midi_number())
        return array


def get_key(key, minor) -> List:
    keys = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']
    if minor:
        return keys[key] + 'm'
    return keys[key]


def detect_key(file) -> Tuple:
    d = [0] * 12

    for track in file.tracks[1:]:
        for msg in track:
            if msg.type == 'note_on':
                print('on', msg.note, msg.time)
            if msg.type == 'note_off':
                print('off', msg.note, msg.time)
                d[msg.note % 12] += msg.time

    print(d)

    def correlation(a, b) -> float:
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
                     0.232998, 4.95122]
    minor_profile = [18.2648, 0.737619, 14.0499, 16.8599, 0.702494, 14.4362, 0.702494, 18.6161, 4.56621, 1.93186,
                     7.37619, 1.75623]

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


def get_possible_chords(key, minor) -> List:
    res = []
    major_table = [
        [0, 2, 4, 5, 7, 9, 11],
        [1, 3, 5, 6, 8, 10, 0],
        [2, 4, 6, 7, 9, 11, 1],
        [3, 5, 7, 8, 10, 0, 2],
        [4, 6, 8, 9, 11, 1, 3],
        [5, 7, 9, 10, 0, 2, 4],
        [6, 8, 10, 11, 1, 3, 5],
        [7, 9, 11, 0, 2, 4, 6],
        [8, 10, 0, 1, 3, 5, 7],
        [9, 11, 1, 2, 4, 6, 8],
        [10, 0, 2, 3, 5, 7, 9],
        [11, 1, 3, 4, 6, 8, 10],
    ]
    minor_table = [
        [0, 2, 3, 5, 7, 8, 10],
        [1, 3, 4, 6, 8, 9, 11],
        [2, 4, 5, 7, 9, 10, 0],
        [3, 5, 6, 8, 10, 11, 1],
        [4, 6, 7, 9, 11, 0, 2],
        [5, 7, 8, 10, 0, 1, 3],
        [6, 8, 9, 11, 1, 2, 4],
        [7, 9, 10, 0, 2, 3, 5],
        [8, 10, 11, 1, 3, 4, 6],
        [9, 11, 0, 2, 4, 5, 7],
        [10, 0, 1, 3, 5, 6, 8],
        [11, 1, 2, 4, 6, 7, 9],
    ]
    major_chords = [
        [0, 3, 4],
        [1, 2, 5],
        [0, 1, 3, 4, 5],
        [0, 1, 2, 4, 5],
    ]
    minor_chords = [
        [2, 5, 6],
        [0, 3, 4],
        [0, 2, 3, 5, 6],
        [0, 2, 3, 4, 6],
    ]
    if minor:
        for i in minor_chords[0]:
            res.append(Chord(Note(minor_table[key][i]), ChordType.major_triad))
            res.append(Chord(Note(minor_table[key][i]), ChordType.first_inverted_major))
            res.append(Chord(Note(minor_table[key][i]), ChordType.second_inverted_major))
        for i in minor_chords[1]:
            res.append(Chord(Note(minor_table[key][i]), ChordType.minor_triad))
            res.append(Chord(Note(minor_table[key][i]), ChordType.first_inverted_minor))
            res.append(Chord(Note(minor_table[key][i]), ChordType.second_inverted_minor))
        res.append(Chord(Note(minor_table[key][1]), ChordType.dim))
        for i in minor_chords[2]:
            res.append(Chord(Note(minor_table[key][i]), ChordType.sus2))
        for i in minor_chords[3]:
            res.append(Chord(Note(minor_table[key][i]), ChordType.sus4))
    else:
        for i in major_chords[0]:
            res.append(Chord(Note(major_table[key][i]), ChordType.major_triad))
            res.append(Chord(Note(major_table[key][i]), ChordType.first_inverted_major))
            res.append(Chord(Note(major_table[key][i]), ChordType.second_inverted_major))
        for i in major_chords[1]:
            res.append(Chord(Note(major_table[key][i]), ChordType.minor_triad))
            res.append(Chord(Note(major_table[key][i]), ChordType.first_inverted_minor))
            res.append(Chord(Note(major_table[key][i]), ChordType.second_inverted_minor))
        res.append(Chord(Note(major_table[key][1]), ChordType.dim))
        for i in major_chords[2]:
            res.append(Chord(Note(major_table[key][i]), ChordType.sus2))
        for i in major_chords[3]:
            res.append(Chord(Note(major_table[key][i]), ChordType.sus4))
    return res


file = mido.MidiFile('input1.mid')
key, minor = detect_key(file)

ticks = 0
for track in file.tracks:
    for msg in track:
        if msg.type == 'note_on' or msg.type == 'note_off':
            ticks += msg.time
beats = math.ceil(ticks / file.ticks_per_beat)
possible_chords = get_possible_chords(key, minor)
population = []
individual = None
population_size = 500
generation_number = 50
for i in range(population_size):
    population.append(random.choices(possible_chords, k=beats))


def fitness(individual) -> int:
    score = 0
    for tick in range(0, ticks, file.ticks_per_beat):
        cur_note = 0
        count = 0
        for track in file.tracks:
            for msg in track:
                if msg.type == 'note_on':
                    count += msg.time
                    if count >= tick:
                        cur_note = Note(msg.note)
                        break
            if cur_note != 0:
                break

        if cur_note != 0:
            notes = []
            for e in individual[tick // file.ticks_per_beat].get_midi_array():
                notes.append(Note(e).get_note())
            if cur_note.get_note() in notes:
                score += 10
    for i in range(0, len(individual) - 1):
        if abs(individual[i].get_midi_array()[0] - individual[i + 1].get_midi_array()[0]) >= 5:
            score -= 10
        if individual[i].get_midi_array()[0] == individual[i + 1].get_midi_array()[0]:
            score -= 5

    return score


def selection() -> List:
    selected = []
    for i in range(population_size):
        a = numpy.random.randint(population_size)
        b = numpy.random.randint(population_size)
        for j in numpy.random.randint(0, population_size, 3):
            if fitness(population[j]) > fitness(population[a]):
                a = j
        for j in numpy.random.randint(0, population_size, 3):
            if fitness(population[j]) > fitness(population[b]):
                b = j
        selected.append([a, b])
    return selected


def crossover(a, b) -> List:
    x = population[a]
    y = population[b]
    child = []
    for i in range(len(x)):
        p = random.randint(1, 2)
        if p == 1:
            child.append(x[i])
        else:
            child.append(y[i])
    return child


def mutation(individual):
    chance_to_mutate = 0.8
    for i in range(len(individual)):
        p = random.random()
        if p >= chance_to_mutate:
            individual[i] = random.choice(possible_chords)


for i in range(generation_number):
    selected = selection()
    next_generation = []
    for pair in selected:
        a, b = pair[0], pair[1]
        new_individual = crossover(a, b)
        mutation(new_individual)
        next_generation.append(new_individual)
    population += next_generation
    population = sorted(population, reverse=True, key=fitness)
    population = population[:population_size]

fittest = population[0]

octave = 3
track = mido.MidiTrack()
for e in fittest:
    for i in e.get_midi_array():
        msg = mido.Message('note_on', note=(octave * 12 + i), velocity=50, time=0)
        track.append(msg)
    msg = mido.Message('note_off', note=(octave * 12 + e.get_midi_array()[0]), velocity=50, time=file.ticks_per_beat)
    track.append(msg)

    for i in e.get_midi_array()[1:]:
        msg = mido.Message('note_off', note=(octave * 12 + i), velocity=50, time=0)
        track.append(msg)

file.tracks.append(track)
file.save('VladislavDanshovOutput1-%s.mid' % get_key(key, minor))
