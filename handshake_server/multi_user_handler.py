from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class MultiUserHandler(RequestHandler):
	def get(self, user_id):
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		connections = []
		try:
			response = con.lookup(user_id)
		except pycps.APIError as e:
			print 'no data available'
			return
		try:
			if response.get_documents:
				print("Found {0} documents: ".format(response.found))
				user = response.get_documents().items()[0][1]
				connections = user['connections'].split(',')
		except:
			print 'fail'

		profiles = []
		for connection in connections:
			try:
				response = con.search(bluetooth_address, connection)
				profiles.append(response)
			except pycps.APIError as e:
				print(e)

		self.write(profiles)
		self.finish()