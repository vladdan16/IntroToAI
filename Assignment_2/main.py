import mido

d = dict()

file = mido.MidiFile('input1.mid')

# for msg in file:
#     if (msg.type == 'note_on'):
#         print('on', msg.note, msg.time)
#     if (msg.type == 'note_off'):
#         print('off', msg.note, msg.time)

for track in file.tracks[1:]:
    for msg in track:
        if (msg.type == 'note_on'):
            print('on', msg.note, msg.time)
        if (msg.type == 'note_off'):
            print('off', msg.note, msg.time)
            if ((msg.note % 12) in d):
                d[msg.note % 12] += msg.time
            else:
                d[msg.note % 12] = msg.time

major_profile = [6.35, 2.23, 3.48, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88]
minor_profile = [6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17]

for i in rande(12):

print(d)
